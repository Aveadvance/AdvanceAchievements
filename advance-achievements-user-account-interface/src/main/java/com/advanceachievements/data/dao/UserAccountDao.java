package com.advanceachievements.data.dao;

import java.util.Optional;

import com.advanceachievements.data.entities.UserAccount;

public interface UserAccountDao extends Repo<UserAccount, Long> {

	Optional<UserAccount> retrieve(String email);
	
}
