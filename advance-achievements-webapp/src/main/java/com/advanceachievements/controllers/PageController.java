package com.advanceachievements.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aveadvance.advancedachievements.data.entities.Workspace;
import com.aveadvance.advancedachievements.data.services.WorkspaceService;

@Controller
public class PageController {
	
	@Autowired
	private WorkspaceService workspaceService;

	@RequestMapping("/")
	@Secured({"ROLE_USER"})
	public String initialPage(HttpServletRequest request) {
		/*
		 * Check if have communities(companies) than list to select personal or community scope.
		 * if (!getCommunityList().empty())
		 * return community space
		 * Check on password to personal scope
		 */
		Workspace workspace = workspaceService.retrieveAll().get(0);
		request.getSession().setAttribute("workspaceId", workspace.getId());
		
		return "redirect:/personal-tasks-page";
	}

	@RequestMapping("/access-denied-page")
	public String accessDeniedPage() {
		return "access-denied-page";
	}
	
}
