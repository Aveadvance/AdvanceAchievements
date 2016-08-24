package com.advanceachievements.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

	@RequestMapping("/")
	public String initialPage() {
		return "index";
	}

	@RequestMapping("/access-denied-page")
	public String accessDeniedPage() {
		return "access-denied-page";
	}
	
}
