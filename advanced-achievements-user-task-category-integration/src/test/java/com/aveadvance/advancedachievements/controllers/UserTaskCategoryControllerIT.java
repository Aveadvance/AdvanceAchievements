package com.aveadvance.advancedachievements.controllers;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
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
import com.aveadvance.advancedachievements.data.entities.UserTaskCategory;
import com.aveadvance.advancedachievements.data.entities.UserTaskState;
import com.aveadvance.advancedachievements.data.entities.Workspace;
import com.aveadvance.advancedachievements.data.services.UserAccountService;
import com.aveadvance.advancedachievements.data.services.UserTaskCategoryService;
import com.aveadvance.advancedachievements.data.services.WorkspaceService;

@ActiveProfiles("development")
@ContextConfiguration(locations={"classpath:com/advanceachievements/configurations/data-test-context.xml"
		, "classpath:com/advanceachievements/configurations/dispatcher-servlet.xml"
		, "classpath:com/advanceachievements/configurations/dao-context.xml"
		, "classpath:com/advanceachievements/configurations/service-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class UserTaskCategoryControllerIT {
	
	private final static Logger logger = Logger.getLogger(UserTaskCategoryControllerIT.class);
	
	private MockMvc mockMvc;
	
	UserAccount testUserAccount = new UserAccount("example@example.com", "12345"
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
	private UserTaskCategoryService userTaskCategoryService;
	
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
		Workspace personalWorkspace = workspaceService.retrieveAll().get(0);
		// TODO: Logger: System.out.println(personalWorkspace.getId()+" "+personalWorkspace.getType());
		String categoryName = "New category";
		mockMvc.perform(MockMvcRequestBuilders.post("/newtaskcategory")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", categoryName)
				.param("workspaceId", Long.toString(personalWorkspace.getId())))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("userTaskCategoryDto"));
		
		UserTaskCategory userTaskCategory = userTaskCategoryService.retrieveAll(personalWorkspace.getId()).get(0);
		assertEquals("Category is saved.", categoryName, userTaskCategory.getName());
		assertEquals("Category has workspace.", personalWorkspace.getId(), userTaskCategory.getWorkspace().getId());
	}
	
	@Test
	@Transactional
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	public void removeTaskCategory() throws Exception {
		
		/* Retrieve personal workspace. */
		Workspace personalWorkspace = workspaceService.retrieveAll().get(0);
		
		/* Create new category. */
		String categoryName = "New category";
		userTaskCategoryService.create(personalWorkspace.getId(), categoryName);
		UserTaskCategory userTaskCategory = userTaskCategoryService.retrieveAll(personalWorkspace.getId()).get(0);
		assertEquals("Category is saved.", categoryName, userTaskCategory.getName());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/deletecategory")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("id", Long.toString(userTaskCategory.getId()))
				.param("workspaceId", Long.toString(personalWorkspace.getId())))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		
		List<UserTaskCategory> userTaskCategories = userTaskCategoryService.retrieveAll(personalWorkspace.getId());
		assertEquals("Category removed.", true, userTaskCategories.isEmpty());
	}
	
	/**
	 * Update user category integration test.
	 * @throws Exception
	 */
	@Test
	@Transactional
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	public void updateUserCategory() throws Exception {
		String newCategoryName = "New name";
		
		Workspace personalWorkspace = workspaceService.retrieveAll().get(0);
		
		/* Create new category. */
		String categoryName = "New category";
		userTaskCategoryService.create(personalWorkspace.getId(), categoryName);
		UserTaskCategory userTaskCategory = userTaskCategoryService.retrieveAll(personalWorkspace.getId()).get(0);
		assertEquals("Category is saved.", categoryName, userTaskCategory.getName());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/updatecategory")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("id", Long.toString(userTaskCategory.getId()))
				.param("name", newCategoryName)
				.param("workspaceId", Long.toString(personalWorkspace.getId())))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("userTaskCategoryDto"));
		
		UserTaskCategory retrievedUserTaskCategory = userTaskCategoryService.retrieveAll(personalWorkspace.getId()).get(0);
		assertEquals("Category updated.", userTaskCategory.getId(), retrievedUserTaskCategory.getId());
		assertEquals("Category updated.", newCategoryName, retrievedUserTaskCategory.getName());
		assertEquals("Category updated.", userTaskCategory.getWorkspace(), retrievedUserTaskCategory.getWorkspace());
	}
	
	@Test
	@Transactional
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	public void tryToRemoveCategoryWithoutWorkspaceParameters() throws Exception {
		
		/* Retrieve personal workspace. */
		Workspace personalWorkspace = workspaceService.retrieveAll().get(0);
		
		/* Create new category. */
		String categoryName = "New category";
		userTaskCategoryService.create(personalWorkspace.getId(), categoryName);
		UserTaskCategory userTaskCategory = userTaskCategoryService.retrieveAll(personalWorkspace.getId()).get(0);
		assertEquals("Category is saved.", categoryName, userTaskCategory.getName());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/deletecategory")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("id", Long.toString(userTaskCategory.getId()))
				.param("workspaceId", "0"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		
		List<UserTaskCategory> userTaskCategories = userTaskCategoryService.retrieveAll(personalWorkspace.getId());
		assertEquals("Category removed.", false, userTaskCategories.isEmpty());
	}
	
	@Test
	@Transactional
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	// TODO Add debug information
	public void createTaskInCategory() throws Exception {
		
		/* Get default private workspace */
		Workspace personalWorkspace = workspaceService.retrieveAll().get(0);

		/* Create new category */
		String categoryName = "Category";
		userTaskCategoryService.create(personalWorkspace.getId(), categoryName);
		List<UserTaskCategory> userTaskCategories = userTaskCategoryService.retrieveAll(personalWorkspace.getId());
		UserTaskCategory userTaskCategory = userTaskCategories.get(0);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/newtask")
				.sessionAttr("workspaceId", personalWorkspace.getId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("title", testUserTask.getTitle())
				.param("description", testUserTask.getDescription())
				.param("priority", "MIDDLE")
				.param("userTaskCategoryId", Long.toString(userTaskCategory.getId())))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("userTaskDto"));
		
		List<UserTask> retrievedUserTasks = userTaskService.retrieve(testUserAccount.getEmail(), personalWorkspace.getId());
		assertEquals("Task should be in category", userTaskCategory
				, retrievedUserTasks.get(0).getCategory().get());
	}
	
	@Test
	@Transactional
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	public void removeTaskThatHasCategory() throws Exception {
		
		logger.debug("***** Entry to removeTaskThatHasCategory()");
		
		/* Get default private workspace */
		Workspace personalWorkspace = workspaceService.retrieveAll().get(0);
		logger.debug("***** Retrieve private workspace, id:" + personalWorkspace.getId());

		/* Create new category */
		String categoryName = "Category";
		userTaskCategoryService.create(personalWorkspace.getId(), categoryName);
		logger.debug("***** Category created, name:" + categoryName);
		
		List<UserTaskCategory> userTaskCategories = userTaskCategoryService.retrieveAll(personalWorkspace.getId());
		UserTaskCategory userTaskCategory = userTaskCategories.get(0);
		logger.debug("***** Retrieve saved category.");
		
		mockMvc.perform(MockMvcRequestBuilders.post("/newtask")
				.sessionAttr("workspaceId", personalWorkspace.getId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("title", testUserTask.getTitle())
				.param("description", testUserTask.getDescription())
				.param("priority", "MIDDLE")
				.param("userTaskCategoryId", Long.toString(userTaskCategory.getId())))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("userTaskDto"));
		logger.debug("***** Request task with category creation.");
		
		UserTask retrievedUserTask = userTaskService.retrieve(testUserAccount.getEmail(), personalWorkspace.getId()).get(0);
		assertEquals("Task should be in category", userTaskCategory
				, retrievedUserTask.getCategory().get());
		
		/* Delete task that has a category */
		userTaskService.delete(personalWorkspace.getId(), retrievedUserTask.getId());
		logger.debug("***** Task with category should be removed.");
		List<UserTask> userTasks = userTaskService.retrieve(testUserAccount.getEmail(), personalWorkspace.getId());
		logger.debug("***** Retrieve category with tasks again.");
		assertEquals("Task is deleted from category", true
				, userTasks.isEmpty());
	}

}
