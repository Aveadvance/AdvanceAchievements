package com.aveadvance.advancedachievements.data.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.aveadvance.advancedachievements.data.entities.Authority;
import com.aveadvance.advancedachievements.data.entities.UserAccount;
import com.aveadvance.advancedachievements.data.services.UserAccountService;

@ActiveProfiles("development")
@ContextConfiguration(locations = {"classpath:com/advanceachievements/configurations/data-test-context.xml"
		, "classpath:com/advanceachievements/configurations/service-context.xml"
		, "classpath:com/advanceachievements/configurations/dao-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class CustomUserDetailsServiceIT {
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private UserDetailsService detailsService;
	
	private UserAccount testUserAccount1 = new UserAccount("example@example.com", "12345");
	
	@Test
	@Transactional
	public void customUserDetailsWork() {
		userAccountService.create(testUserAccount1.getEmail(), testUserAccount1.getPassword());
		userAccountService.retrieve(testUserAccount1.getEmail());
		UserDetails user = detailsService.loadUserByUsername(testUserAccount1.getEmail());
		assertEquals("User details are correctly loaded", testUserAccount1.getEmail(), user.getUsername());
		assertEquals("User details are correctly loaded", testUserAccount1.getPassword(), user.getPassword());
		for (Authority authority : testUserAccount1.getAuthorities()) {
			assertTrue("User details are correctly loaded", user.getAuthorities().contains(new SimpleGrantedAuthority(authority.name())));
		}
	}
	
}
