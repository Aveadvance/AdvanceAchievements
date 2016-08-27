package com.advanceachievements.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

	@RequestMapping("/")
	public String initialPage() {
		/*
		 * Check if have communities(companies) than list to select personal or community scope.
		 * if (!getCommunityList().empty())
		 * return community space
		 * Check on password to personal scope
		 */
		return "redirect:/personal-tasks-page";
	}

	@RequestMapping("/access-denied-page")
	public String accessDeniedPage() {
		return "access-denied-page";
	}
	
}
