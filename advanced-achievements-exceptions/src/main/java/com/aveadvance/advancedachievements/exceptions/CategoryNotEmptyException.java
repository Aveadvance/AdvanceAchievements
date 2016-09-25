package com.aveadvance.advancedachievements.exceptions;

public class CategoryNotEmptyException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CategoryNotEmptyException() {
		super();
	}
	
	public CategoryNotEmptyException(String message) {
		super(message);
	}

}
