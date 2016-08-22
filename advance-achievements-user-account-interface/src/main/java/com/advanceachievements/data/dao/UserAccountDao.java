package com.advanceachievements.data.dao;

import com.advanceachievements.data.entities.UserAccount;

public interface UserAccountDao extends Repo<UserAccount, Long> {

	UserAccount retrieve(String email);
	
}
