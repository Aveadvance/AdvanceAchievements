package com.advanceachievements.data.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.advanceachievements.data.entities.UserAccount;

@ActiveProfiles("development")
@ContextConfiguration(locations={"classpath:com/advanceachievements/configurations/data-test-context.xml"
		, "classpath:com/advanceachievements/configurations/dao-context.xml"
		, "classpath:com/advanceachievements/configurations/services-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class UserAccountServiceIT {
	
	@Autowired
	UserAccountService userAccountService;
	
	@Test
	@Transactional
	public void createUserFromInputParameters() {
		String email = "example@example.com";
		String password = "pass12345";
		userAccountService.create(email, password);
		UserAccount userAccount = userAccountService.retrieve(email);
		assertEquals("Email must be identical.", email, userAccount.getEmail());
		assertEquals("Password must be identical.", password, userAccount.getPassword());
	}
	
//	@Test
//	@Transactional
//	public void createTwoAccountsWithIdenticalEmails() {
//	}

}
