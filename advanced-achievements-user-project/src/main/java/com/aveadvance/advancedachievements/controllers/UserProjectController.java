package com.aveadvance.advancedachievements.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aveadvance.advancedachievements.data.dto.UserProjectDto;
import com.aveadvance.advancedachievements.data.services.UserProjectService;
import com.aveadvance.advancedachievements.exceptions.ExceptionsDto;
import com.aveadvance.advancedachievements.exceptions.ProjectNotEmptyException;

@Controller
public class UserProjectController {
	
	@Autowired
	private UserProjectService userProjectService;
	
	@RequestMapping("/create-project-page")
	public String createProjectPage() {
		return "advanced-achievements-user-project/create-project-page";
	}
	
	@RequestMapping("/update-project-page")
	public String updateProjectPage(Long id, Model model) {
//		long workspaceId = (Long) request.getSession().getAttribute("parentWorkspaceId");
		userProjectService.retrieve(id).ifPresent(project -> {
			model.addAttribute("userProjectToUpdate", project);
		});
		return "advanced-achievements-user-project/create-project-page";
	}

	@RequestMapping("/newproject")
	public String newProject(@Valid UserProjectDto userProjectDto, HttpServletRequest request) {
		long workspaceId = (Long) request.getSession().getAttribute("workspaceId");
		if (userProjectDto.getId() == 0)
			userProjectService.create(workspaceId, userProjectDto.getName(), userProjectDto.getDescription());
		else
			userProjectService.update(userProjectDto.getId(), userProjectDto.getName(), userProjectDto.getDescription());
		return "redirect:/";
	}

	@RequestMapping("/deleteproject")
	public String newProject(long id, HttpServletRequest request) {
		long workspaceId = (Long) request.getSession().getAttribute("parentWorkspaceId");
		try {
			userProjectService.delete(workspaceId, id);
		} catch (ProjectNotEmptyException ex) {
			ExceptionsDto exceptionsDto = new ExceptionsDto();
			exceptionsDto.addException("global", ex.getMessage());
			request.getSession().setAttribute("exceptionsDto", exceptionsDto);
		}
		return "redirect:/personal-tasks-page?type=home";
	}
	
}
