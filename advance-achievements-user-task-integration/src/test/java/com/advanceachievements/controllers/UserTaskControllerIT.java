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

import com.advanceachievements.data.entities.Authority;
import com.advanceachievements.data.entities.UserAccount;
import com.advanceachievements.data.services.UserAccountService;
import com.advanceachievements.data.services.UserTaskCategoryService;
import com.advanceachievements.data.services.UserTaskService;
import com.advanceachievements.data.services.WorkspaceService;
import com.aveadvance.advancedachievements.data.entities.Priority;
import com.aveadvance.advancedachievements.data.entities.UserTask;
import com.aveadvance.advancedachievements.data.entities.UserTaskCategory;
import com.aveadvance.advancedachievements.data.entities.UserTaskState;
import com.aveadvance.advancedachievements.data.entities.Workspace;

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
	private UserTaskCategoryService userTaskCategoryService;
	
	@Autowired
	private WorkspaceService workspaceService;
	
	UserAccount testUserAccount = new UserAccount("example@example.com", "12345"
			, new HashSet<>(Arrays.asList(Authority.ROLE_USER)), true);
	
	UserTask correctUserTask = new UserTask("New task", "My new task"
			, Priority.MIDDLE, testUserAccount, UserTaskState.TO_DO, LocalDateTime.now());
	
	@Before
	public void before() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		
		/*
		 * Task should have owner, authorized user is taken for this purpose by default.
		 */

		/* Create account to save the task. */
		userAccountService.create(testUserAccount);
	}
	
	@Test
	@Transactional
	@WithMockUser(username="example@example.com", password="12345",authorities={"ROLE_USER"})
	public void createNewTaskCorrectly() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/newtask")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("title", correctUserTask.getTitle())
				.param("description", correctUserTask.getDescription())
				.param("priority", correctUserTask.getPriority().name()))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("userTaskDto"));
		
		
		UserTask recievedUserTask = userTaskService.retrieve(testUserAccount.getEmail()).get(0);
		assertEquals("Task saved in database.", correctUserTask.getTitle(), recievedUserTask.getTitle());
		assertEquals("Task saved in database.", correctUserTask.getDescription(), recievedUserTask.getDescription());
		assertEquals("Task saved in database.", correctUserTask.getPriority(), recievedUserTask.getPriority());
	}
	
	@Test
	public void withoutTaskTitle() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/newtask")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("description", correctUserTask.getDescription())
				.param("priority", correctUserTask.getPriority().name()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("userTaskDto", "title"));
	}
	
	@Test
	public void blankTaskTitle() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/newtask")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("title", "")
				.param("description", correctUserTask.getDescription())
				.param("priority", correctUserTask.getPriority().name()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("userTaskDto", "title"));
	}
	
	@Test
	public void nonExistingPriority() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/newtask")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("title", correctUserTask.getTitle())
				.param("description", correctUserTask.getDescription())
				.param("priority", "PRIORITY"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("userTaskDto", "priority"));
	}
	
	@Test
	@Transactional
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	public void ruSymbolicTittleAndDescription() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/newtask")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("title", "Ыъзфычсмюб")
				.param("description", "Новый текст")
				.param("priority", "MIDDLE"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("userTaskDto"));
		
		List<UserTask> retrievedUserTasks = userTaskService.retrieve(testUserAccount.getEmail());
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
	public void createNewTaskCategory() throws Exception {
		Workspace privateWorkspace = workspaceService.retrieveAll().get(0);
		// TODO: Logger: System.out.println(privateWorkspace.getId()+" "+privateWorkspace.getType());
		String categoryName = "New category";
		mockMvc.perform(MockMvcRequestBuilders.post("/newtaskcategory")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", categoryName)
				.param("workspaceId", Long.toString(privateWorkspace.getId())))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("userTaskCategoryDto"));
		
		UserTaskCategory userTaskCategory = userTaskCategoryService.retrieveAll(privateWorkspace.getId()).get(0);
		assertEquals("Category is saved.", categoryName, userTaskCategory.getName());
		assertEquals("Category has workspace.", privateWorkspace.getId(), userTaskCategory.getWorkspace().getId());
	}
	
	@Test
	@Transactional
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	// TODO Add debug information
	public void createTaskInCategory() throws Exception {
		String categoryName = "Category";
		Workspace privateWorkspace = workspaceService.retrieveAll().get(0);
		userTaskCategoryService.create(privateWorkspace.getId(), categoryName);
		List<UserTaskCategory> userTaskCategories = userTaskCategoryService.retrieveAll(privateWorkspace.getId());
		UserTaskCategory userTaskCategory = userTaskCategories.get(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/newtask")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("title", correctUserTask.getTitle())
				.param("description", correctUserTask.getDescription())
				.param("priority", "MIDDLE")
				.param("userTaskCategoryId", Long.toString(userTaskCategory.getId())))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("userTaskDto"));
		
		List<UserTask> retrievedUserTasks = userTaskService.retrieve(testUserAccount.getEmail());
		userTaskCategory = userTaskCategoryService.retrieveAll(privateWorkspace.getId()).get(0);
		assertEquals("Task should be in category", retrievedUserTasks.get(0)
				, userTaskCategory.getUserTasks().get(0));
	}

}
