package com.aveadvance.advancedachievements.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aveadvance.advanceachievements.data.dto.UserTaskCategoryDto;
import com.aveadvance.advancedachievements.data.services.UserTaskCategoryService;

@Controller
public class UserTaskCategoryController {
	
	@Autowired
	private UserTaskCategoryService userTaskCategoryService;
	
	@RequestMapping("/create-task-category-page")
	public String createNewTaskCategoryPage() {
		return "advanced-achievements-user-task-category/create-task-category-page";
	}

	@RequestMapping("/newtaskcategory")
	public String create(HttpServletRequest request, @Valid UserTaskCategoryDto userTaskCategoryDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return createNewTaskCategoryPage();
		}
		
		long workspaceId = (Long) request.getSession().getAttribute("workspaceId");
		
		userTaskCategoryService.create(workspaceId, userTaskCategoryDto.getName());
		return "redirect:/personal-tasks-page";
	}

	@RequestMapping("/deletecategory")
	public String delete(long workspaceId, long id) {
		if (workspaceId < 1 || id < 1) {
			return "redirect:/personal-tasks-page";
		}
		
		userTaskCategoryService.delete(workspaceId, id);
		return "redirect:/personal-tasks-page";
	}
	
	@RequestMapping("/updatecategory")
	public String updateCategory(HttpServletRequest request, @Valid UserTaskCategoryDto userTaskCategoryDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "redirect:/personal-tasks-page";
		}
		
		long workspaceId = (Long) request.getSession().getAttribute("workspaceId");
		
		userTaskCategoryService.update(workspaceId, userTaskCategoryDto.getId(), userTaskCategoryDto.getName());
		return "redirect:/personal-tasks-page";
	}

}
