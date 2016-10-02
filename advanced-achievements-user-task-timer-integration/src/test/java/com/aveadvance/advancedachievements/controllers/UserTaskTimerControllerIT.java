package com.aveadvance.advancedachievements.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

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

import com.aveadvance.advancedachievements.data.entities.Authority;
import com.aveadvance.advancedachievements.data.entities.Priority;
import com.aveadvance.advancedachievements.data.entities.UserAccount;
import com.aveadvance.advancedachievements.data.entities.UserTask;
import com.aveadvance.advancedachievements.data.entities.UserTaskState;
import com.aveadvance.advancedachievements.data.entities.UserTaskTimer;
import com.aveadvance.advancedachievements.data.entities.Workspace;
import com.aveadvance.advancedachievements.data.services.UserAccountService;
import com.aveadvance.advancedachievements.data.services.UserTaskService;
import com.aveadvance.advancedachievements.data.services.UserTaskTimerService;
import com.aveadvance.advancedachievements.data.services.WorkspaceService;

@ActiveProfiles("development")
@ContextConfiguration(locations={"classpath:com/aveadvance/advancedachievements/configurations/data-test-context.xml"
		, "classpath:com/aveadvance/advancedachievements/configurations/dispatcher-servlet.xml"
		, "classpath:com/aveadvance/advancedachievements/configurations/dao-context.xml"
		, "classpath:com/aveadvance/advancedachievements/configurations/security-context.xml"
		, "classpath:com/aveadvance/advancedachievements/configurations/service-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class UserTaskTimerControllerIT {
	
	private MockMvc mockMvc;
	
	private UserAccount testUserAccount = new UserAccount("example@example.com", "12345"
			, new HashSet<>(Arrays.asList(Authority.ROLE_USER)), true);
	
	private UserTask testUserTask = new UserTask(null, "New task", "Correct user task.", Priority.MIDDLE
			, testUserAccount, UserTaskState.TO_DO, LocalDateTime.now());
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private WorkspaceService workspaceService;
	
	@Autowired
	private UserTaskService userTaskService;
	
	@Autowired
	private UserTaskTimerService userTaskTimerService;
	
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
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	public void createNewTaskCategory() throws Exception {
		
		/* Retrieve the private workspace */
		Workspace personalWorkspace = workspaceService.retrieveAll().get(0);
		
		/* Create a new task */
		userTaskService.create(personalWorkspace.getId(), testUserTask.getTitle(), testUserTask.getDescription()
				, testUserTask.getPriority());
		
		UserTask userTask = userTaskService.retrieve("example@example.com", personalWorkspace.getId()).get(0);
		
		assertEquals("User task was created.", testUserTask.getTitle(), userTask.getTitle());
		
		UserTaskTimer userTaskTimer = userTaskTimerService.retrieve("example@example.com", personalWorkspace.getId()).get(0);

		assertEquals("User task timer for user task was created too.", userTask.getId(), userTaskTimer.getUserTask().getId());

		/* Start the task timer. */
		LocalDateTime start = LocalDateTime.now();
		
		mockMvc.perform(MockMvcRequestBuilders.post("/engagetasktimer/"+userTask.getId())
				.sessionAttr("workspaceId", personalWorkspace.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(jsonPath("$.status", is("started")));

		LocalDateTime stop = LocalDateTime.now();
		
		userTaskTimer = userTaskTimerService.retrieve("example@example.com", personalWorkspace.getId()).get(0);
		
		assertTrue("Task timer started."
				, userTaskTimer.getStartDate().isEqual(start) 
				|| userTaskTimer.getStartDate().isAfter(start) 
				&& userTaskTimer.getStartDate().isBefore(stop));
		
		/* Wait a bit. */
		Thread.sleep(3000);
		
		/* Stop the task timer */
		mockMvc.perform(MockMvcRequestBuilders.post("/engagetasktimer/"+userTask.getId())
				.sessionAttr("workspaceId", personalWorkspace.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(jsonPath("$.status", is("stopped")));
		
		userTaskTimer = userTaskTimerService.retrieve("example@example.com", personalWorkspace.getId()).get(0);
		
		assertNull("Task stopped.", userTaskTimer.getStartDate());
	}

	@Test
	@Transactional
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	public void deleteUserTaskTimerWhenUserTaskWasDeleted() {
		
		/* Retrieve the private workspace */
		Workspace personalWorkspace = workspaceService.retrieveAll().get(0);
		
		/* Create a new task */
		userTaskService.create(personalWorkspace.getId(), testUserTask.getTitle(), testUserTask.getDescription()
				, testUserTask.getPriority());
		
		UserTask userTask = userTaskService.retrieve("example@example.com", personalWorkspace.getId()).get(0);
		
		assertEquals("User task was created.", testUserTask.getTitle(), userTask.getTitle());
		
		UserTaskTimer userTaskTimer = userTaskTimerService.retrieve("example@example.com", personalWorkspace.getId()).get(0);

		assertEquals("User task timer for user task was created too.", userTask.getId(), userTaskTimer.getUserTask().getId());
		
		/* Delete the task. */
		userTaskService.delete(personalWorkspace.getId(), userTask.getId());

		assertEquals("User task was deleted.", 0, userTaskService.retrieve("example@example.com", personalWorkspace.getId()).size());
		assertEquals("User task timer was deleted too.", 0, userTaskTimerService.retrieve("example@example.com", personalWorkspace.getId()).size());
	}

}
