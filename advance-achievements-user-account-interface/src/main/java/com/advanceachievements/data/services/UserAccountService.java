package com.advanceachievements.data.services;

import com.advanceachievements.data.entities.UserAccount;

public interface UserAccountService {

	void create(String email, String password);

	UserAccount retrieve(String email);

}
