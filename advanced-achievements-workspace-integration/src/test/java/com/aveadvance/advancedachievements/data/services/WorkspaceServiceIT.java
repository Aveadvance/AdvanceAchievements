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

import com.aveadvance.advancedachievements.data.entities.Workspace;
import com.aveadvance.advancedachievements.data.entities.WorkspaceType;

@ActiveProfiles("development")
@ContextConfiguration(locations={"classpath:com/advanceachievements/configurations/data-test-context.xml"
		, "classpath:com/advanceachievements/configurations/dao-context.xml"
		, "classpath:com/advanceachievements/configurations/service-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class WorkspaceServiceIT {
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private WorkspaceService workspaceService;

	@Before
	public void before() {
		userAccountService.create("example@example.com", "12345");
	}
	
	@Test
	@Transactional
	@WithMockUser(username="example@example.com", authorities={"ROLE_USER"})
	public void testOnNegativePrimaryKey() {
		
		workspaceService.create(WorkspaceType.PRIVATE);
		workspaceService.create(WorkspaceType.PRIVATE);
		workspaceService.create(WorkspaceType.PRIVATE);
		
		List<Workspace> workspaces = workspaceService.retrieveAll();
		assertEquals("Workspaces are created.", 4, workspaces.size());
		
		workspaces.forEach(workspace -> {
			assertEquals("Primary key should be positive. (Oracle feature)", true, workspace.getId() > 0);
		});
	}
}
