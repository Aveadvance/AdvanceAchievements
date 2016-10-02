package com.aveadvance.advancedachievements.data.services;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.aveadvance.advancedachievements.data.entities.Authority;
import com.aveadvance.advancedachievements.data.entities.Priority;
import com.aveadvance.advancedachievements.data.entities.UserAccount;
import com.aveadvance.advancedachievements.data.entities.UserTask;
import com.aveadvance.advancedachievements.data.entities.UserTaskState;
import com.aveadvance.advancedachievements.data.entities.Workspace;

@ActiveProfiles("development")
@ContextConfiguration(locations={"classpath:com/aveadvance/advancedachievements/configurations/service-context.xml"
		, "classpath:com/aveadvance/advancedachievements/configurations/security-context.xml"
		, "classpath:com/aveadvance/advancedachievements/configurations/data-test-context.xml"
		, "classpath:com/aveadvance/advancedachievements/configurations/dao-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class UserTaskTimerServiceIT {
	
	private UserAccount testUserAccount = new UserAccount("example@example.com", "12345"
			, new HashSet<>(Arrays.asList(Authority.ROLE_USER)), true);
	
	private UserTask testUserTask = new UserTask(null, "Title of the task", "Description of task"
			, Priority.MIDDLE, testUserAccount, UserTaskState.TO_DO, LocalDateTime.now());
	
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
		/*
		 * Create task method needs authorization and the previously created user.
		 * Values of parameters in @WithMockUser and in the previously created user should correlate.
		 */
		userAccountService.create(testUserAccount.getEmail(), testUserAccount.getPassword());
	}

	@Test
	@Transactional
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	public void deleteAccuracy() {
		
		/* Retrieve the private workspace */
		Workspace personalWorkspace = workspaceService.retrieveAll().get(0);
		
		/* Create a new tasks */
		userTaskService.create(personalWorkspace.getId(), testUserTask.getTitle(), testUserTask.getDescription()
				, testUserTask.getPriority());
		userTaskService.create(personalWorkspace.getId(), testUserTask.getTitle(), testUserTask.getDescription()
				, testUserTask.getPriority());
		userTaskService.create(personalWorkspace.getId(), testUserTask.getTitle(), testUserTask.getDescription()
				, testUserTask.getPriority());
		
		List<UserTask> userTasks = userTaskService.retrieve("example@example.com", personalWorkspace.getId());
		
		assertEquals("User tasks was created.", 3
				,  userTasks.size());

		assertEquals("User task timers for user task was created too.", 3
				, userTaskTimerService.retrieve("example@example.com", personalWorkspace.getId()).size());
		
		/* Delete the task. */
		userTaskService.delete(personalWorkspace.getId(), userTasks.get(0).getId());

		assertEquals("User tasks was created.", 2
				,  userTaskService.retrieve("example@example.com", personalWorkspace.getId()).size());

		assertEquals("User task timers for user task was created too.", 2
				, userTaskTimerService.retrieve("example@example.com", personalWorkspace.getId()).size());
	}

}
