package com.aveadvance.advancedachievements.data.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.advanceachievements.data.dao.UserAccountDao;
import com.aveadvance.advancedachievements.data.entities.Authority;
import com.aveadvance.advancedachievements.data.entities.UserAccount;
import com.aveadvance.advancedachievements.data.entities.Workspace;
import com.aveadvance.advancedachievements.data.entities.WorkspaceType;

@Service
public class UserAccountServiceBasic implements UserAccountService {
	
	@Autowired
	private UserAccountDao userAccountDao;
	
	@Autowired
	private WorkspaceService workspaceService;

	@Override
	@Transactional
	public boolean create(String email, String password) {
		UserAccount userAccount = new UserAccount(email, password);
		return create(userAccount);
	}

	@Override
	@Secured({"ROLE_ADMIN"})
	public boolean create(String email, String password, Set<Authority> authorities) {
		UserAccount userAccount = new UserAccount(email, password, authorities);
		return create(userAccount);
	}

	@Override
	@Transactional(readOnly=true)
	public Optional<UserAccount> retrieve(String email) {
		return userAccountDao.retrieve(email);
	}

	@Transactional
	private boolean create(UserAccount userAccount) {
		boolean result = retrieve(userAccount.getEmail()).isPresent();
		if (!result) {
			userAccountDao.create(userAccount);
			workspaceService.create(new Workspace(WorkspaceType.PRIVATE, new HashSet<>(Arrays.asList(userAccount))));
		}
		return !result;
	}

	@Override
	@Transactional
	public void update(UserAccount userAccount) {
		userAccountDao.update(userAccount);
	}

}
