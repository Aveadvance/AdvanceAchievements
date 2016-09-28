package com.aveadvance.advancedachievements.data.services;

import java.util.Optional;
import java.util.Set;

import com.aveadvance.advancedachievements.data.entities.Authority;
import com.aveadvance.advancedachievements.data.entities.UserAccount;

public interface UserAccountService {

	boolean create(String email, String password);

	boolean create(String email, String password, Set<Authority> authorities);

	Optional<UserAccount> retrieve(String email);

	void update(UserAccount userAccount);

}
