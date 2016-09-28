package com.aveadvance.advancedachievements.data.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aveadvance.advancedachievements.data.dao.WorkspaceDao;
import com.aveadvance.advancedachievements.data.entities.UserAccount;
import com.aveadvance.advancedachievements.data.entities.Workspace;
import com.aveadvance.advancedachievements.data.entities.WorkspaceType;
import com.aveadvance.advancedachievements.exceptions.UserAccountNotFoundException;

@Service
public class WorkspaceServiceBasic implements WorkspaceService {
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private WorkspaceDao workspaceDao;

	@Override
	@Secured({"ROLE_USER"})
	@Transactional(readOnly=true)
	public List<Workspace> retrieveAll() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserAccount userAccount = userAccountService.retrieve(auth.getName()).get();
		return workspaceDao.retrieveAll(userAccount);
	}

	@Override
	@Secured({"ROLE_USER"})
	@Transactional
	public void create(WorkspaceType type) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		Optional<UserAccount> userOpt = userAccountService.retrieve(auth.getName());
		UserAccount user = userOpt.orElseThrow(
				() -> new UserAccountNotFoundException("User account is not found. Please try again."));
		
		Set<UserAccount> users = new HashSet<>();
		users.add(user);
		
		Workspace workspace = new Workspace(type, users);
		workspaceDao.create(workspace);
	}

	@Override
	@Transactional(readOnly=true)
	public Optional<Workspace> retrieve(long workspaceId) {
		return workspaceDao.retrieve(workspaceId);
	}

}
