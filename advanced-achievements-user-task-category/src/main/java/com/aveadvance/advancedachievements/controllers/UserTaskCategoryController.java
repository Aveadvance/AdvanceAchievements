package com.aveadvance.advancedachievements.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aveadvance.advancedachievements.data.dto.UserTaskCategoryDto;
import com.aveadvance.advancedachievements.data.entities.UserTaskCategory;
import com.aveadvance.advancedachievements.data.services.UserTaskCategoryService;
import com.aveadvance.advancedachievements.exceptions.CategoryNotEmptyException;
import com.aveadvance.advancedachievements.exceptions.ExceptionsDto;

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
		if (userTaskCategoryDto.getId() == 0) {
			userTaskCategoryService.create(workspaceId, userTaskCategoryDto.getName());
		} else {
			userTaskCategoryService.update(workspaceId, userTaskCategoryDto.getId(), userTaskCategoryDto.getName());
		}
		return "redirect:/personal-tasks-page";
	}
	
	@RequestMapping("/update-task-category-page")
	public String updateTaskCategoryPage(long id, HttpServletRequest request, Model model) {
		long workspaceId = (Long) request.getSession().getAttribute("workspaceId");
		Optional<UserTaskCategory> userTaskCategory = userTaskCategoryService.retrieve(workspaceId, id);
		userTaskCategory.ifPresent(category -> {
			model.addAttribute("userTaskCategoryToUpdate", category);
		});
		return createNewTaskCategoryPage();
	}

	@RequestMapping("/deletetaskcategory")
	public String delete(long id, HttpServletRequest request, Model model) {
		ExceptionsDto exceptions = new ExceptionsDto();
		long workspaceId = (Long)request.getSession().getAttribute("workspaceId");
		if (workspaceId < 1 || id < 1) {
			return "redirect:/personal-tasks-page";
		}
		
		try {
			userTaskCategoryService.delete(workspaceId, id);
		} catch (CategoryNotEmptyException ex) {
			exceptions.addException("global", ex.getMessage());
			
			/* Set session attribute */
			request.getSession().setAttribute("exceptionsDto", exceptions);
		}
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
