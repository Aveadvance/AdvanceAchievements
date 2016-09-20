package com.advanceachievements.data.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.advanceachievements.data.dao.UserAccountDao;
import com.advanceachievements.data.entities.UserAccount;
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
		boolean result = retrieve(email).isPresent();
		if (!result) {
			UserAccount userAccount = new UserAccount(email, password);
			userAccountDao.create(userAccount);
		}
		return !result;
	}

	@Override
	@Transactional(readOnly=true)
	public Optional<UserAccount> retrieve(String email) {
		return userAccountDao.retrieve(email);
	}

	@Override
	@Transactional
	public boolean create(UserAccount userAccount) {
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
