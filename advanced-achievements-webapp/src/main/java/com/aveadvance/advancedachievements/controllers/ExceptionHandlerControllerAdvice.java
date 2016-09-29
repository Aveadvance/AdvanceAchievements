package com.aveadvance.advancedachievements.controllers;

import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {
	
	@ExceptionHandler(value=SQLGrammarException.class)
	public String sqlGrammarException(Exception ex) {
		ex.printStackTrace();
		return "redirect:/persistence-exception-page";
	}
	
	@ExceptionHandler(value=ConstraintViolationException.class)
	public String constraintViolationException(Exception ex) {
		ex.printStackTrace();
		return "redirect:/persistence-exception-page";
	}

}
