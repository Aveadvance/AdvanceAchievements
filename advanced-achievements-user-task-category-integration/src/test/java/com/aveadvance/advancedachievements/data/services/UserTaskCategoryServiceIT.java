package com.aveadvance.advancedachievements.data.services;

import static org.junit.Assert.assertEquals;

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

import com.aveadvance.advancedachievements.data.entities.Priority;
import com.aveadvance.advancedachievements.data.entities.UserTask;
import com.aveadvance.advancedachievements.data.entities.UserTaskCategory;
import com.aveadvance.advancedachievements.data.entities.UserTaskState;
import com.aveadvance.advancedachievements.data.entities.Workspace;
import com.aveadvance.advancedachievements.data.services.UserTaskService;
import com.aveadvance.advancedachievements.exceptions.CategoryNotEmptyException;

@ActiveProfiles("development")
@ContextConfiguration(locations={"classpath:com/aveadvance/advancedachievements/configurations/data-test-context.xml"
		, "classpath:com/aveadvance/advancedachievements/configurations/dao-context.xml"
		, "classpath:com/aveadvance/advancedachievements/configurations/service-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class UserTaskCategoryServiceIT {
	
	private UserTask testTask = new UserTask(null, "New task", "TaskDescription", Priority.MIDDLE
			, null, null, UserTaskState.TO_DO, null);
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private WorkspaceService workspaceService;
	
	@Autowired
	private UserTaskCategoryService userTaskCategoryService;
	
	@Autowired
	private UserTaskService userTaskService;
	
	@Before
	public void before() {
		userAccountService.create("example@example.com", "12345");
	}
	
	/**
	 * Prevent from deleting a category with tasks.
	 */
	@Test(expected=CategoryNotEmptyException.class)
	@Transactional
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	public void tryToRemoveTaskCategoryWithTasks() {
		Workspace personalWorkspace = workspaceService.retrieveAll().get(0);
		
		/* Create new category. */
		String categoryName = "New category";
		userTaskCategoryService.create(personalWorkspace.getId(), categoryName);
		UserTaskCategory userTaskCategory = userTaskCategoryService.retrieveAll(personalWorkspace.getId()).get(0);
		assertEquals("Category is saved.", categoryName, userTaskCategory.getName());
		
		/* Create task in the category */
		userTaskService.create(personalWorkspace.getId(), testTask.getTitle(), testTask.getDescription()
				, testTask.getPriority(), userTaskCategory.getId());
		
		UserTask recievedUserTask = userTaskService.retrieve("example@example.com", personalWorkspace.getId()).get(0);
		
		assertEquals("Task created.", testTask.getTitle()
				, recievedUserTask.getTitle());
		
		/* Try to delete */
		userTaskCategoryService.delete(personalWorkspace.getId(), userTaskCategory.getId());
	}
	
	@Test
	@Transactional
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	public void testOnNegativePrimaryKey() {
		Workspace personalWorkspace = workspaceService.retrieveAll().get(0);
		
		userTaskCategoryService.create(personalWorkspace.getId(), "Category1");
		userTaskCategoryService.create(personalWorkspace.getId(), "Category2");
		userTaskCategoryService.create(personalWorkspace.getId(), "Category3");
		
		List<UserTaskCategory> categories = userTaskCategoryService.retrieveAll(personalWorkspace.getId());
		assertEquals("Categories saved.", 3, categories.size());
		categories.forEach(category -> {
			assertEquals("Categories saved.", true, category.getId() > 0);
		});
		
	}
}
