package com.advanceachievements.data.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.advanceachievements.data.dao.UserAccountDao;
import com.advanceachievements.data.entities.UserAccount;

@Service
public class UserAccountServiceBasic implements UserAccountService {
	
	@Autowired
	UserAccountDao userAccountDao;

	@Override
	@Transactional
	public void create(String email, String password) {
		UserAccount userAccount = new UserAccount(email, password);
		userAccountDao.create(userAccount);
	}

	@Override
	public Optional<UserAccount> retrieve(String email) {
		return userAccountDao.retrieve(email);
	}

	@Override
	public void create(UserAccount userAccount) {
		userAccountDao.create(userAccount);
	}

}
