package com.aveadvance.advancedachievements.controllers;

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
import com.aveadvance.advancedachievements.data.entities.UserProject;
import com.aveadvance.advancedachievements.data.entities.Workspace;
import com.aveadvance.advancedachievements.data.services.UserAccountService;
import com.aveadvance.advancedachievements.data.services.UserProjectService;
import com.aveadvance.advancedachievements.data.services.UserTaskService;
import com.aveadvance.advancedachievements.data.services.WorkspaceService;

@ActiveProfiles("development")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:com/aveadvance/advancedachievements/configurations/dispatcher-servlet.xml"
		, "classpath:com/aveadvance/advancedachievements/configurations/data-test-context.xml"
		, "classpath:com/aveadvance/advancedachievements/configurations/dao-context.xml"
		, "classpath:com/aveadvance/advancedachievements/configurations/service-context.xml"})
@WebAppConfiguration
public class UserProjectControllerIT {
	
	private MockMvc mockMvc;
	
	private UserAccount testUserAccount = new UserAccount("example@example.com", "12345"
			, new HashSet<>(Arrays.asList(Authority.ROLE_USER)), true);
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private WorkspaceService workspaceService;
	
	@Autowired
	private UserProjectService userProjectService;
	
	@Autowired
	private UserTaskService userTaskService;
	
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
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	@Transactional
	public void createUserProject() throws Exception {
		Workspace workspace = workspaceService.retrieveAll().get(0);

		LocalDateTime start = LocalDateTime.now();
		
		mockMvc.perform(MockMvcRequestBuilders.post("/newproject")
				.sessionAttr("workspaceId", workspace.getId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", "Test project.")
				.param("description", "Project description"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("userProjectDto"));

		LocalDateTime stop = LocalDateTime.now();
		
		UserProject retrievedProject = userProjectService.retrieveAll(workspace.getId()).get(0);
		
		assertEquals("Project created", "Test project.", retrievedProject.getName());
		assertEquals("Project created", "Project description", retrievedProject.getDescription());
		assertTrue("Project's creation time"
				, retrievedProject.getCreationDate().isEqual(start) 
				|| retrievedProject.getCreationDate().isAfter(start) 
				&& retrievedProject.getCreationDate().isBefore(stop));
		assertEquals("Project created", 2, workspaceService.retrieveAll().size());
	}
	
	@Test
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	@Transactional
	public void deleteProject() throws Exception {
		Workspace workspace = workspaceService.retrieveAll().get(0);
		
		userProjectService.create(workspace.getId(), "Test project", null);
		
		UserProject retrievedProject = userProjectService.retrieveAll(workspace.getId()).get(0);
		
		assertEquals("Project created", "Test project", retrievedProject.getName());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/deleteproject")
				.sessionAttr("workspaceId", workspace.getId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("id", Long.toString(retrievedProject.getId())))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		
		List<UserProject> projects = userProjectService.retrieveAll(workspace.getId());
		List<Workspace> workspaces = workspaceService.retrieveAll();
		
		assertEquals("Project was deleted.", 0, projects.size());
		assertEquals("Project was deleted.", 1, workspaces.size());
		assertEquals("Project was deleted.", 1, workspaces.get(0).getUserAccounts().size());
	}
	
	@Test
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	@Transactional
	public void deleteProjectWithTasksAndCategories() throws Exception {
		Workspace workspace = workspaceService.retrieveAll().get(0);
		
		userProjectService.create(workspace.getId(), "Test project", null);
		
		UserProject retrievedProject = userProjectService.retrieveAll(workspace.getId()).get(0);
		
		/* Create task in project */
		userTaskService.create(retrievedProject.getProjectWorkspace().getId(), "Task title", "Task description.", Priority.MIDDLE);
		
		assertEquals("Project created", "Test project", retrievedProject.getName());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/deleteproject")
				.sessionAttr("workspaceId", workspace.getId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("id", Long.toString(retrievedProject.getId())))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		
		List<UserProject> projects = userProjectService.retrieveAll(workspace.getId());
		
		assertEquals("Project shouldn't be deleted.", 1, projects.size());
	}
	
	@Test
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	@Transactional
	public void updateProject() throws Exception {
		Workspace workspace = workspaceService.retrieveAll().get(0);
		
		userProjectService.create(workspace.getId(), "Project project", "Description");
		
		UserProject createdProject = userProjectService.retrieveAll(workspace.getId()).get(0);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/newproject")
				.sessionAttr("workspaceId", workspace.getId())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("id", Long.toString(createdProject.getId()))
				.param("name", "Test project.")
				.param("description", "Project description"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("userProjectDto"));
		
		UserProject retrievedProject = userProjectService.retrieveAll(workspace.getId()).get(0);
		
		assertEquals("Project updated.", "Test project.", retrievedProject.getName());
		assertEquals("Project updated.", "Project description", retrievedProject.getDescription());
		assertEquals("Project updated.", createdProject.getId(), retrievedProject.getId());
		assertEquals("Project updated.", createdProject.getProjectWorkspace(), retrievedProject.getProjectWorkspace());
		assertEquals("Project updated.", createdProject.getCreationDate(), retrievedProject.getCreationDate());
	}
	
}
