package com.advanceachievements.data.services;

import java.util.Optional;

import com.advanceachievements.data.entities.UserAccount;

public interface UserAccountService {

	void create(String email, String password);

	Optional<UserAccount> retrieve(String email);

	void create(UserAccount userAccount);

}
