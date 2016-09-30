package com.aveadvance.advancedachievements.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

	@RequestMapping("/")
	@Secured({"ROLE_USER"})
	public String initialPage(String type, Long id, HttpServletRequest request) {
		/*
		 * Check if have communities(companies) than list to select personal or community scope.
		 * if (!getCommunityList().empty())
		 * return community space
		 * Check on password to personal scope
		 */
		
		return "redirect:/personal-tasks-page?type=home";
	}

	@RequestMapping("/access-denied-page")
	public String accessDeniedPage() {
		return "access-denied-page";
	}
	
}
