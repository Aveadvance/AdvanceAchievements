package com.advanceachievements.controllers;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.advanceachievements.data.services.UserTaskService;
import com.aveadvance.advancedachievements.data.entities.Authority;
import com.aveadvance.advancedachievements.data.entities.Priority;
import com.aveadvance.advancedachievements.data.entities.UserAccount;
import com.aveadvance.advancedachievements.data.entities.UserTask;
import com.aveadvance.advancedachievements.data.entities.UserTaskState;
import com.aveadvance.advancedachievements.data.entities.Workspace;
import com.aveadvance.advancedachievements.data.services.UserAccountService;
import com.aveadvance.advancedachievements.data.services.WorkspaceService;

@ActiveProfiles("development")
@ContextConfiguration(locations={"classpath:com/advanceachievements/configurations/dispatcher-servlet.xml"
		, "classpath:com/advanceachievements/configurations/service-context.xml"
		, "classpath:com/advanceachievements/configurations/data-test-context.xml"
		, "classpath:com/advanceachievements/configurations/dao-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class UserTaskControllerIT {
	
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private UserTaskService userTaskService;
	
	@Autowired
	private WorkspaceService workspaceService;
	
	UserAccount testUserAccount = new UserAccount("example@example.com", "12345"
			, new HashSet<>(Arrays.asList(Authority.ROLE_USER)), true);
	
	UserTask testUserTask = new UserTask(null, "New task", "My new task"
			, Priority.MIDDLE, testUserAccount, UserTaskState.TO_DO, LocalDateTime.now());
	
	@Before
	public void before() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		
		/*
		 * Task should have owner, authorized user is taken for this purpose by default.
		 */

		/* Create account to save the task. */
		userAccountService.create(testUserAccount.getEmail(), testUserAccount.getPassword());
	}
	
	@Test
	@Transactional
	@WithMockUser(username="example@example.com", password="12345",authorities={"ROLE_USER"})
	public void createNewTaskCorrectly() throws Exception {
		Workspace workspace = workspaceService.retrieveAll().get(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/newtask")
				.sessionAttr("workspaceId", workspace.getId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("title", testUserTask.getTitle())
				.param("description", testUserTask.getDescription())
				.param("priority", testUserTask.getPriority().name()))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("userTaskDto"));
		
		
		UserTask recievedUserTask = userTaskService.retrieve(testUserAccount.getEmail(), workspace.getId()).get(0);
		assertEquals("Task saved in database.", testUserTask.getTitle(), recievedUserTask.getTitle());
		assertEquals("Task saved in database.", testUserTask.getDescription(), recievedUserTask.getDescription());
		assertEquals("Task saved in database.", testUserTask.getPriority(), recievedUserTask.getPriority());
	}
	
	@Test
	public void withoutTaskTitle() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/newtask")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("description", testUserTask.getDescription())
				.param("priority", testUserTask.getPriority().name()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("userTaskDto", "title"));
	}
	
	@Test
	public void blankTaskTitle() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/newtask")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("title", "")
				.param("description", testUserTask.getDescription())
				.param("priority", testUserTask.getPriority().name()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("userTaskDto", "title"));
	}
	
	@Test
	public void nonExistingPriority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/newtask")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("title", testUserTask.getTitle())
				.param("description", testUserTask.getDescription())
				.param("priority", "PRIORITY"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("userTaskDto", "priority"));
	}
	
	@Test
	@Transactional
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	public void ruSymbolicTittleAndDescription() throws Exception {
		Workspace workspace = workspaceService.retrieveAll().get(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/newtask")
				.sessionAttr("workspaceId", workspace.getId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("workspaceId", Long.toString(workspace.getId()))
				.param("title", "Ыъзфычсмюб")
				.param("description", "Новый текст")
				.param("priority", "MIDDLE"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("userTaskDto"));
		
		List<UserTask> retrievedUserTasks = userTaskService.retrieve(testUserAccount.getEmail(), workspace.getId());
		assertEquals("Russian symbolic in title", "Ыъзфычсмюб", retrievedUserTasks.get(0).getTitle());
		assertEquals("Russian symbolic in description", "Новый текст", retrievedUserTasks.get(0).getDescription());
	}
	
	@Test
	public void largeTaskTitle(){}
	
	@Test
	public void largeDescription(){}

	@Test
	@Transactional
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	public void deleteTask() throws Exception {
		Workspace workspace = workspaceService.retrieveAll().get(0);
		
		userTaskService.create(workspace.getId(), testUserTask.getTitle(), testUserTask.getTitle()
				, testUserTask.getPriority());
		
		List<UserTask> userTasks = userTaskService.retrieve("example@example.com", workspace.getId());
		
		assertEquals("Task created.", testUserTask.getTitle(), userTasks.get(0).getTitle());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/deleteusertask")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("workspaceId", Long.toString(workspace.getId()))
				.param("id", Long.toString(userTasks.get(0).getId())))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		
		userTasks = userTaskService.retrieve("example@example.com", workspace.getId());
		
		assertEquals("Task deleted.", true, userTasks.isEmpty());
	}

	/**
	 * Test task update.
	 * @throws Exception
	 */
	@Test
	@Transactional
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	public void updateTask() throws Exception {
		Workspace workspace = workspaceService.retrieveAll().get(0);
		
		userTaskService.create(workspace.getId(), testUserTask.getTitle(), testUserTask.getTitle()
				, testUserTask.getPriority());
		
		UserTask userTask = userTaskService.retrieve("example@example.com", workspace.getId()).get(0);
		
		assertEquals("Task created.", testUserTask.getTitle(), userTask.getTitle());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/updateusertask")
				.sessionAttr("workspaceId", workspace.getId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("id", Long.toString(userTask.getId()))
				.param("title", "New title")
				.param("description", "New new new text.")
				.param("priority", "HIGH"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("userTaskDto"));
		
		UserTask updatedUserTask = userTaskService.retrieve("example@example.com", workspace.getId()).get(0);

		assertEquals("Task updated.", userTask.getId(), updatedUserTask.getId());
		assertEquals("Task updated.", "New title", updatedUserTask.getTitle());
		assertEquals("Task updated.", "New new new text.", updatedUserTask.getDescription());
		assertEquals("Task updated.", Priority.HIGH, updatedUserTask.getPriority());
		assertEquals("Task updated.", userTask.getOwner(), updatedUserTask.getOwner());
		assertEquals("Task updated.", userTask.getState(), updatedUserTask.getState());
		assertEquals("Task updated.", userTask.getCategory(), updatedUserTask.getCategory());
		assertEquals("Task updated.", userTask.getCreationDate(), updatedUserTask.getCreationDate());
	}

}
