package com.advanceachievements.controllers;

import org.hibernate.exception.SQLGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {
	
	@ExceptionHandler(value=SQLGrammarException.class)
	public String dbExceptions(Exception ex) {
		ex.printStackTrace();
		return "redirect:/persistence-exception-page";
	}

}
