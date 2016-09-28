package com.aveadvance.advancedachievements.data.services;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.advanceachievements.data.dao.UserAccountDao;
import com.aveadvance.advancedachievements.data.entities.Authority;
import com.aveadvance.advancedachievements.data.entities.UserAccount;
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
	@Secured({"ROLE_USER"})
	private boolean create(UserAccount userAccount) {
		boolean result = retrieve(userAccount.getEmail()).isPresent();
		if (!result) {
			userAccountDao.create(userAccount);
			
			/* Automatic authentication in due to lack of email authentication */
			/* TODO: Is it safe? */
			Authentication auth = new UsernamePasswordAuthenticationToken(userAccount.getEmail(), null, null);
			SecurityContextHolder.getContext().setAuthentication(auth);
			
			workspaceService.create(WorkspaceType.PRIVATE);
		}
		return !result;
	}

	@Override
	@Transactional
	public void update(UserAccount userAccount) {
		userAccountDao.update(userAccount);
	}

}
