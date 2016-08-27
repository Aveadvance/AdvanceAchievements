package com.advanceachievements.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.advanceachievements.data.dto.UserTaskCategoryDto;
import com.advanceachievements.data.dto.UserTaskDto;
import com.advanceachievements.data.entities.UserTask;
import com.advanceachievements.data.services.UserTaskCategoryService;
import com.advanceachievements.data.services.UserTaskService;

@Controller
public class UserTaskController {
	
	@Autowired
	private UserTaskService userTaskService;
	
	@Autowired
	private UserTaskCategoryService userTaskCategoryService;
	
	@RequestMapping("/personal-tasks-page")
	@Secured("hasRole(ROLE_USER)")
	public String personalTasksPage(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<UserTask> personalTasks = userTaskService.retrieve(auth.getName());
		model.addAttribute("personalTasks", personalTasks);
		return "advance-achievements-user-task/personal-tasks-page";
	}
	
	@RequestMapping("/create-task-page")
	public String personalTasksPage() {
		return "advance-achievements-user-task/create-task-page";
	}

	@RequestMapping("/newtask")
	public String create(@Valid UserTaskDto userTaskDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "/create-task-page";
		}
		if (userTaskDto.getUserTaskCategoryId() == 0)
			userTaskService.create(userTaskDto.getTitle(), userTaskDto.getDescription()
				, userTaskDto.getPriority());
		else
			userTaskService.create(userTaskDto.getTitle(), userTaskDto.getDescription()
					, userTaskDto.getPriority(), userTaskDto.getUserTaskCategoryId());
		return "redirect:/personal-tasks-page";
	}

	@RequestMapping("/newtaskcategory")
	public String create(@Valid UserTaskCategoryDto userTaskCategoryDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "/create-task-category-page";
		}
		userTaskCategoryService.create(userTaskCategoryDto.getWorkspaceId(), userTaskCategoryDto.getName());
		return "redirect:/personal-tasks-page";
	}

}
