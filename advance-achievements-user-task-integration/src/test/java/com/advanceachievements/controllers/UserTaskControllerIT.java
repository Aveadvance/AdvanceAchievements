package com.advanceachievements.controllers;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

import com.advanceachievements.data.entities.Priority;
import com.advanceachievements.data.entities.UserTask;
import com.advanceachievements.data.services.UserTaskService;

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
	private UserTaskService userTaskService;
	
	UserTask correctUserTask = new UserTask("New task", "My new task", Priority.MIDDLE);
	
	@Before
	public void before() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
	@Transactional
	public void createNewTaskCorrectly() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/newtask")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("title", correctUserTask.getTitle())
				.param("description", correctUserTask.getDescription())
				.param("priority", correctUserTask.getPriority().name()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("userTaskDto"));
		
		UserTask recievedUserTask = userTaskService.retrieve(1).get();
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
	public void largeTaskTitle(){}
	
	@Test
	public void largeDescription(){}

}
