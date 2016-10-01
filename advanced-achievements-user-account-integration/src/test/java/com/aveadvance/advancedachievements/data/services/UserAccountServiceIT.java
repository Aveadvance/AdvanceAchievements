package com.aveadvance.advancedachievements.data.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.aveadvance.advancedachievements.data.entities.Authority;
import com.aveadvance.advancedachievements.data.entities.UserAccount;

@ActiveProfiles("development")
@ContextConfiguration(locations = { "classpath:com/aveadvance/advancedachievements/configurations/data-test-context.xml",
		"classpath:com/aveadvance/advancedachievements/configurations/dao-context.xml",
		"classpath:com/aveadvance/advancedachievements/configurations/service-context.xml",
		"classpath:com/aveadvance/advancedachievements/configurations/security-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class UserAccountServiceIT {

	@Autowired
	UserAccountService userAccountService;

	private UserAccount userAccount1 = new UserAccount("example@example.com", "pass12345");

	@Test
	@Transactional
	public void createUserFromInputParameters() {
		userAccountService.create(userAccount1.getEmail(), userAccount1.getPassword());
		UserAccount userAccount = userAccountService.retrieve(userAccount1.getEmail()).get();
		assertEquals("Email must be identical.", userAccount1.getEmail(), userAccount.getEmail());
		assertTrue("Password must be encripted.", userAccount.getPassword().length() > 0);
		assertNotEquals("Password must be encripted.", userAccount1.getPassword(), userAccount.getPassword());
	}

	@Test
	@Transactional
	public void defaultAuthority() {
		userAccountService.create(userAccount1.getEmail(), userAccount1.getPassword());
		Optional<UserAccount> userAccount = userAccountService.retrieve(userAccount1.getEmail());
		Set<Authority> authorities = userAccount.get().getAuthorities();
		assertTrue("Should be only one default authority", authorities.size() == 1);
		assertTrue("Default authority should be ROLE_USER", authorities.contains(Authority.ROLE_USER));
	}

	@Test
	@Transactional
	public void multipleAuthorities() {
		Set<Authority> authorities = new HashSet<>();
		authorities.add(Authority.ROLE_USER);
		authorities.add(Authority.ROLE_ADMIN);
		userAccountService.create(userAccount1.getEmail(), userAccount1.getPassword(), authorities);
		Optional<UserAccount> retrievedUserAccount = userAccountService.retrieve(userAccount1.getEmail());
		assertTrue("UserAccount has multiple roles", retrievedUserAccount.get().getAuthorities().contains(Authority.ROLE_USER));
		assertTrue("UserAccount has multiple roles", retrievedUserAccount.get().getAuthorities().contains(Authority.ROLE_ADMIN));
	}
	
	@Test
	@Transactional
	public void doubledAuthorities() {
		// For reminding about doubled authority roles.
		
		Set<Authority> authorities = new HashSet<>();
		authorities.add(Authority.ROLE_USER);
		authorities.add(Authority.ROLE_USER);
		userAccountService.create(userAccount1.getEmail(), userAccount1.getPassword(), authorities);
		Optional<UserAccount> retrievedUserAccount = userAccountService.retrieve(userAccount1.getEmail());
		assertEquals("UserAccount shouldn't have doubled roles.", 1, retrievedUserAccount.get().getAuthorities().size());
	}

	@Test
	@Transactional
	public void tryToRecieveNonExistingUser() {
		assertFalse("User should not exist.", userAccountService.retrieve("NonExistingEmail@example.com").isPresent());
	}

	@Test
	@Transactional
	public void createTwoAccountsWithIdenticalEmails() throws Throwable {
		boolean result = userAccountService.create(userAccount1.getEmail(), userAccount1.getPassword());
		assertTrue("User created", result && userAccountService.retrieve(userAccount1.getEmail()).isPresent());
		result = userAccountService.create(userAccount1.getEmail(), userAccount1.getPassword());
		assertTrue("User exists", !result && userAccountService.retrieve(userAccount1.getEmail()).isPresent());
	}
	
	@Test
	@Transactional
	public void userAccountCreationDate() {
		LocalDateTime start = LocalDateTime.now();
		userAccountService.create(userAccount1.getEmail(), userAccount1.getPassword());
		LocalDateTime stop = LocalDateTime.now();
		Optional<UserAccount> retrievedUserAccount = userAccountService.retrieve(userAccount1.getEmail());
		assertTrue("Right creation time was set up"
				, retrievedUserAccount.get().getCreationDate().isEqual(start) 
				|| retrievedUserAccount.get().getCreationDate().isAfter(start) 
				&& retrievedUserAccount.get().getCreationDate().isBefore(stop));
		
	}
	
	@Test
	@Transactional
	public void userAccountEnabledByDefault() {
		userAccountService.create(userAccount1.getEmail(), userAccount1.getPassword());
		assertTrue("User account enabled by default", userAccountService.retrieve(userAccount1.getEmail()).get().isEnabled());
	}
	
	@Test
	@Transactional
	public void disableUserAccount() {
		userAccountService.create(userAccount1.getEmail(), userAccount1.getPassword());
		UserAccount retrieved = userAccountService.retrieve(userAccount1.getEmail()).get();
		assertTrue("User account enabled by default.", retrieved.isEnabled());
		userAccountService.update(new UserAccount(retrieved.getId(), retrieved.getEmail()
				, retrieved.getPassword(), retrieved.getAuthorities(), false));
		retrieved = userAccountService.retrieve(retrieved.getEmail()).get();
		assertFalse("User should be disabled.", retrieved.isEnabled());
	}
	
	@Test
	@Transactional
	public void testOnNegativeId() {
		userAccountService.create(userAccount1.getEmail(), userAccount1.getPassword());
		userAccountService.create("user2@example.com", "$@Dsdd");
		userAccountService.create("user3@example.com", "$@D1823y8sdd");
		assertEquals("Id should be positive number. (Oracle feature)", true
				,userAccountService.retrieve(userAccount1.getEmail()).get().getId() > 0);
		assertEquals("Id should be positive number. (Oracle feature)", true
				,userAccountService.retrieve("user2@example.com").get().getId() > 0);
		assertEquals("Id should be positive number. (Oracle feature)", true
				,userAccountService.retrieve("user3@example.com").get().getId() > 0);
	}

	@Test
	@Transactional
	public void passwordEncoding() {
		userAccountService.create(userAccount1.getEmail(), userAccount1.getPassword());
		UserAccount userAccount = userAccountService.retrieve(userAccount1.getEmail()).get();
		assertTrue("Password must be encripted.", userAccount.getPassword().length() > 0);
		assertNotEquals("Password must be encripted.", userAccount1.getPassword(), userAccount.getPassword());
	}

}
