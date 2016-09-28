package com.aveadvance.advancedachievements.exceptions;

public class UserAccountNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public UserAccountNotFoundException() {
		super();
	}
	
	public UserAccountNotFoundException(String message) {
		super(message);
	}

}
