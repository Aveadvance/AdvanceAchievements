package com.advanceachievements.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="user_accounts")
public class UserAccount {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_accounts_seq")
	@SequenceGenerator(name="user_accounts_seq", sequenceName="user_accounts_seq", allocationSize=1)
	private long id;
	
	@Column(name="email")
	private String email;
	
	@Column(name="password")
	private String password;
	
	public UserAccount() {}

	public UserAccount(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

}
