package com.aveadvance.advancedachievements.exceptions;

public class ProjectNotEmptyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ProjectNotEmptyException() {
		super();
	}
	
	public ProjectNotEmptyException(String message) {
		super(message);
	}

}
