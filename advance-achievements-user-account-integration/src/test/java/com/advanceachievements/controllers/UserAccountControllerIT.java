package com.advanceachievements.controllers;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.advanceachievements.data.dto.UserAccountDto;
import com.advanceachievements.data.services.UserAccountService;
import com.aveadvance.advancedachievements.data.entities.UserAccount;

@ActiveProfiles("development")
@ContextConfiguration(locations ={"classpath:com/advanceachievements/configurations/dispatcher-servlet.xml"
		, "classpath:com/advanceachievements/configurations/service-context.xml"
		, "classpath:com/advanceachievements/configurations/dao-context.xml"
		, "classpath:com/advanceachievements/configurations/data-test-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class UserAccountControllerIT {
	
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private UserAccountService userAccountService;
	
	private UserAccountDto correctAccountDto = new UserAccountDto("example@example.com", "12345");
	
	@Before
	public void before() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
	@Transactional
	public void createNewAccountMappingCorrectInput() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/newaccount")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email",correctAccountDto.getEmail()).param("password", correctAccountDto.getPassword()))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("userAccountDto"));
		
		// Retrieve saved account from database
		UserAccount retrievedAccount = userAccountService.retrieve(correctAccountDto.getEmail()).get();
		assertEquals("Account should be saved in the database"
				, correctAccountDto.getEmail(), retrievedAccount.getEmail());
		assertEquals("Account should be saved in the database"
				, correctAccountDto.getPassword(), retrievedAccount.getPassword());
	}
	
	@Test
	@Transactional
	public void withEmptyEmailParameter() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/newaccount")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", "")
				.param("password", correctAccountDto.getPassword()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("userAccountDto", "email"));
	}
	
	@Test
	@Transactional
	public void withEmptyPasswordParameter() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/newaccount")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", correctAccountDto.getEmail())
				.param("password", ""))
		.andExpect(status().isOk())
		.andExpect(model().attributeHasFieldErrors("userAccountDto", "password"));
	}
	
	@Test
	@Transactional
	public void withBlankEmailParameter() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/newaccount")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", " ")
				.param("password", correctAccountDto.getPassword()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("userAccountDto", "email"));
	}
	
	@Test
	@Transactional
	public void withBlankPasswordParameter() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/newaccount")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", correctAccountDto.getEmail())
				.param("password", " "))
		.andExpect(status().isOk())
		.andExpect(model().attributeHasFieldErrors("userAccountDto", "password"));
	}
	
	@Test
	@Transactional
	public void withEmailParameterAbsence() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/newaccount")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("password", correctAccountDto.getPassword()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("userAccountDto", "email"));
	}
	
	@Test
	@Transactional
	public void withPasswordParameterAbsence() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/newaccount")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", correctAccountDto.getEmail()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("userAccountDto", "password"));
	}
	
	@Test
	@Transactional
	public void withTestEmailParameter1() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/newaccount")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", "example")
				.param("password", correctAccountDto.getPassword()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("userAccountDto", "email"));
	}
	
	@Test
	@Transactional
	public void withTestEmailParameter2() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/newaccount")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", "example@example")
				.param("password", correctAccountDto.getPassword()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("userAccountDto", "email"));
	}

	@Test
	@Transactional
	public void withTestEmailParameter3() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/newaccount")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", "example@example.")
				.param("password", correctAccountDto.getPassword()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("userAccountDto", "email"));
	}

	@Test
	@Transactional
	public void withShortPassword() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/newaccount")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", correctAccountDto.getEmail())
				.param("password", "1234"))
		.andExpect(status().isOk())
		.andExpect(model().attributeHasFieldErrors("userAccountDto", "password"));
	}
	
}
