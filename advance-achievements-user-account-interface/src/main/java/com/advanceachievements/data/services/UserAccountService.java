package com.advanceachievements.data.services;

import java.util.Optional;

import com.advanceachievements.data.entities.UserAccount;

public interface UserAccountService {

	boolean create(String email, String password);

	Optional<UserAccount> retrieve(String email);

	boolean create(UserAccount userAccount);

	void update(UserAccount userAccount);

}
