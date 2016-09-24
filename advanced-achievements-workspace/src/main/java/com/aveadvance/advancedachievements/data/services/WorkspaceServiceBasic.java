package com.aveadvance.advancedachievements.data.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.aveadvance.advancedachievements.data.dao.WorkspaceDao;
import com.aveadvance.advancedachievements.data.entities.UserAccount;
import com.aveadvance.advancedachievements.data.entities.Workspace;

@Service
public class WorkspaceServiceBasic implements WorkspaceService {
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private WorkspaceDao workspaceDao;

	@Override
	public List<Workspace> retrieveAll() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserAccount userAccount = userAccountService.retrieve(auth.getName()).get();
		return workspaceDao.retrieveAll(userAccount);
	}

	@Override
	public void create(Workspace workspace) {
		workspaceDao.create(workspace);
	}

	@Override
	public Optional<Workspace> retrieve(long workspaceId) {
		return workspaceDao.retrieve(workspaceId);
	}

}
