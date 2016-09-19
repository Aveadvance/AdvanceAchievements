package com.advanceachievements.data.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.advanceachievements.data.entities.Authority;
import com.advanceachievements.data.entities.UserAccount;

/**
 * Service for Spring Security database authorization and authentication.
 * @author Aveadvance
 *
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserAccountService userAccountService;

	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserAccount> userAccountOptional = userAccountService.retrieve(username);
		UserAccount userAccount = userAccountOptional.orElseThrow(() -> new UsernameNotFoundException("User not found."));
		return new User(userAccount.getEmail(), userAccount.getPassword()
				,userAccount.isEnabled() , true, true, true, buildUserAuthorities(userAccount.getAuthorities()));
	}
	
	private List<GrantedAuthority> buildUserAuthorities(Set<Authority> authorities) {
		List<GrantedAuthority> result = new ArrayList<>();
		for (Authority authority : authorities) {
			result.add(new SimpleGrantedAuthority(authority.name()));
		}
		return result;
	}

}
