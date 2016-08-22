package com.advanceachievements.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advanceachievements.data.dao.UserAccountDao;
import com.advanceachievements.data.entities.UserAccount;

@Service
public class UserAccountServiceBasic implements UserAccountService {
	
	@Autowired
	UserAccountDao userAccountDao;

	@Override
	public void create(String email, String password) {
		UserAccount userAccount = new UserAccount(email, password);
		userAccountDao.create(userAccount);
	}

	@Override
	public UserAccount retrieve(String email) {
		return userAccountDao.retrieve(email);
	}

}
