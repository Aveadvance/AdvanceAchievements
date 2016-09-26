package com.advanceachievements.data.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import com.aveadvance.advancedachievements.data.services.UserAccountService;
import com.aveadvance.advancedachievements.data.services.WorkspaceService;


@ActiveProfiles("development")
@ContextConfiguration(locations={"classpath:com/advanceachievements/configurations/service-context.xml"
		, "classpath:com/advanceachievements/configurations/data-test-context.xml"
		, "classpath:com/advanceachievements/configurations/dao-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class UserTaskServiceIT {
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private WorkspaceService workspaceService;
	
	@Autowired
	private UserTaskService userTaskService;
	
	UserAccount testUserAccount = new UserAccount("example@example.com", "12345"
			, new HashSet<>(Arrays.asList(Authority.ROLE_USER)), true);
	
	private UserTask testUserTask = new UserTask(null, "Title of the task", "Description of task"
			, Priority.MIDDLE, testUserAccount, UserTaskState.TO_DO, LocalDateTime.now());

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
	@WithMockUser(username="example@example.com",authorities={"ROLE_USER"})
	public void defaultOwnerOfUserTask() {
		Workspace workspace = workspaceService.retrieveAll().get(0);
		userTaskService.create(workspace.getId(), testUserTask.getTitle(), testUserTask.getDescription(), testUserTask.getPriority());
		UserTask retrievedUserTask = userTaskService.retrieve(testUserAccount.getEmail(), workspace.getId()).get(0);
		assertEquals("Default owner should be authenticated user", "example@example.com", retrievedUserTask.getOwner().getEmail());
	}
	
	@Test
	@Transactional
	@WithMockUser(username="example@example.com",authorities={"ROLE_USER"})
	public void userTaskCreationDate() {
		Workspace workspace = workspaceService.retrieveAll().get(0);
		LocalDateTime start = LocalDateTime.now();
		userTaskService.create(workspace.getId(), testUserTask.getTitle(), testUserTask.getDescription(), testUserTask.getPriority());
		UserTask retrievedUserTask = userTaskService.retrieve(testUserAccount.getEmail(), workspace.getId()).get(0);
		LocalDateTime stop = LocalDateTime.now();
		assertTrue("Right creation time was set up"
				, retrievedUserTask.getCreationDate().isEqual(start) 
				|| retrievedUserTask.getCreationDate().isAfter(start) 
				&& retrievedUserTask.getCreationDate().isBefore(stop));
		
	}
	
	@Test
	@Transactional
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	public void userTaskDefaultState() {
		Workspace workspace = workspaceService.retrieveAll().get(0);
		userTaskService.create(workspace.getId(), testUserTask.getTitle(), testUserTask.getDescription(), testUserTask.getPriority());
		UserTask retrievedUserTask = userTaskService.retrieve(testUserAccount.getEmail(), workspace.getId()).get(0);
		assertEquals("Default task state should be TO_DO.", UserTaskState.TO_DO, retrievedUserTask.getState());
	}
	
	@Test
	@Transactional
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	public void createMultipleTasks() {
		Workspace workspace = workspaceService.retrieveAll().get(0);
		userTaskService.create(workspace.getId(), testUserTask.getTitle(), testUserTask.getDescription(), testUserTask.getPriority());
		userTaskService.create(workspace.getId(), testUserTask.getTitle(), testUserTask.getDescription(), testUserTask.getPriority());
		userTaskService.create(workspace.getId(), testUserTask.getTitle(), testUserTask.getDescription(), testUserTask.getPriority());
		List<UserTask> retrievedUserTasks = userTaskService.retrieve(testUserAccount.getEmail(), workspace.getId());
		assertEquals("Default task state should be TO_DO.", 3, retrievedUserTasks.size());
	}
	
	@Test
	@Transactional
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	public void ruSymbolicTittleAndDescription() {
		Workspace workspace = workspaceService.retrieveAll().get(0);
		userTaskService.create(workspace.getId(), "Ыъзфычсмюб", "Новый текст", testUserTask.getPriority());
		List<UserTask> retrievedUserTasks = userTaskService.retrieve(testUserAccount.getEmail(), workspace.getId());
		assertEquals("Russian symbolic in title", "Ыъзфычсмюб", retrievedUserTasks.get(0).getTitle());
		assertEquals("Russian symbolic in description", "Новый текст", retrievedUserTasks.get(0).getDescription());
	}
	
	public void deleteCategoryWhichHaveTwoDifferentUsers() {}
	
	@Test
	public void testOnNegativePrimaryKey() {
		assertEquals("Oracle feature",true,false);
	}
	
	public void testToUpdateUserTaskWithingFriendsWorkspace() {
		/* Task with not valid workspace */
	}
	
}
