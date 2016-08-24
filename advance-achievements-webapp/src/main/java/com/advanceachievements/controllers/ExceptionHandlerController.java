package com.advanceachievements.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ExceptionHandlerController {
	
	@RequestMapping("/persistence-exception-page")
	public String persistenceExceptionPage() {
		return "persistence-exception-page";
	}

}
