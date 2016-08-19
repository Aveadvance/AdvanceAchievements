package com.advanceachievements.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ActiveProfiles("development")
@ContextConfiguration(locations ={"classpath:com/advanceachievements/configurations/data-test-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class UserAccountControllerTest {

	@Autowired
	ApplicationContext context;
	
	@Test
	public void test() {
	}
	
}
