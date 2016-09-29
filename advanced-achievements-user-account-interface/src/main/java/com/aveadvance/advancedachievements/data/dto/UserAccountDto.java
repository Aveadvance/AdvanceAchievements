package com.aveadvance.advancedachievements.data.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class UserAccountDto {
	
	@NotNull
	@NotBlank
	@Email(regexp=".*\\@.*\\..*")
	private String email;
	
	@NotNull
	@NotBlank
	@Size(min=5, max=50)
	private String password;
	
	public UserAccountDto() {}

	public UserAccountDto(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
