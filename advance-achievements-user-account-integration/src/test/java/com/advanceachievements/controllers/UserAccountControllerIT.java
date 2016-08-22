package com.advanceachievements.controllers;

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
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("development")
@ContextConfiguration(locations ={"classpath:com/advanceachievements/configurations/dispatcher-servlet.xml"
		, "classpath:com/advanceachievements/configurations/security-context.xml"
		, "classpath:com/advanceachievements/configurations/data-test-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class UserAccountControllerIT {
	
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Before
	public void before() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
	public void createNewAccountMappingCorrectInput() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/newaccount")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email","example@example.com").param("password", "12345"))
		.andExpect(MockMvcResultMatchers.status().isOk());
//		.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("account"));
	}
	
}
