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

import com.advanceachievements.data.entities.Authority;
import com.advanceachievements.data.entities.Priority;
import com.advanceachievements.data.entities.UserAccount;
import com.advanceachievements.data.entities.UserTask;
import com.advanceachievements.data.entities.UserTaskState;

@ActiveProfiles("development")
@ContextConfiguration(locations={"classpath:com/advanceachievements/configurations/service-context.xml"
		, "classpath:com/advanceachievements/configurations/data-test-context.xml"
		, "classpath:com/advanceachievements/configurations/dao-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class UserTaskServiceIT {
	
	@Autowired
	private UserTaskService userTaskService;
	
	@Autowired
	private UserAccountService userAccountService;
	
	UserAccount testUserAccount = new UserAccount("example@example.com", "12345"
			, new HashSet<>(Arrays.asList(Authority.ROLE_USER)), true);
	
	private UserTask testUserTask = new UserTask("Title of the task", "Description of task"
			, Priority.MIDDLE, testUserAccount, UserTaskState.TO_DO, LocalDateTime.now());

	@Before
	public void before() {
		/*
		 * Create task method needs authorization and the previously created user.
		 * Values of parameters in @WithMockUser and in the previously created user should correlate.
		 */
		userAccountService.create(testUserAccount);
	}
	
	@Test
	@Transactional
	@WithMockUser(username="example@example.com",authorities={"ROLE_USER"})
	public void defaultOwnerOfUserTask() {
		userTaskService.create(testUserTask.getTitle(), testUserTask.getDescription(), testUserTask.getPriority());
		UserTask retrievedUserTask = userTaskService.retrieve(testUserAccount.getEmail()).get(0);
		assertEquals("Default owner should be authenticated user", "example@example.com", retrievedUserTask.getOwner().getEmail());
	}
	
	@Test
	@Transactional
	@WithMockUser(username="example@example.com",authorities={"ROLE_USER"})
	public void userTaskCreationDate() {
		LocalDateTime start = LocalDateTime.now();
		userTaskService.create(testUserTask.getTitle(), testUserTask.getDescription(), testUserTask.getPriority());
		UserTask retrievedUserTask = userTaskService.retrieve(testUserAccount.getEmail()).get(0);
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
		userTaskService.create(testUserTask.getTitle(), testUserTask.getDescription(), testUserTask.getPriority());
		UserTask retrievedUserTask = userTaskService.retrieve(testUserAccount.getEmail()).get(0);
		assertEquals("Default task state should be TO_DO.", UserTaskState.TO_DO, retrievedUserTask.getState());
	}
	
	@Test
	@Transactional
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	public void createMultipleTasks() {
		userTaskService.create(testUserTask.getTitle(), testUserTask.getDescription(), testUserTask.getPriority());
		userTaskService.create(testUserTask.getTitle(), testUserTask.getDescription(), testUserTask.getPriority());
		userTaskService.create(testUserTask.getTitle(), testUserTask.getDescription(), testUserTask.getPriority());
		List<UserTask> retrievedUserTasks = userTaskService.retrieve(testUserAccount.getEmail());
		assertEquals("Default task state should be TO_DO.", 3, retrievedUserTasks.size());
	}
	
	@Test
	@Transactional
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	public void ruSymbolicTittleAndDescription() {
		userTaskService.create("Ыъзфычсмюб", "Новый текст", testUserTask.getPriority());
		List<UserTask> retrievedUserTasks = userTaskService.retrieve(testUserAccount.getEmail());
		assertEquals("Russian symbolic in title", "Ыъзфычсмюб", retrievedUserTasks.get(0).getTitle());
		assertEquals("Russian symbolic in description", "Новый текст", retrievedUserTasks.get(0).getDescription());
	}
	
	public void deleteCategoryWhichHaveTwoDifferentUsers() {}
	
}
