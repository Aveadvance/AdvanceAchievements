package com.advanceachievements.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.advanceachievements.data.dto.UserTaskDto;
import com.advanceachievements.data.services.UserTaskService;
import com.aveadvance.advancedachievements.data.entities.UserTask;
import com.aveadvance.advancedachievements.data.entities.UserTaskCategory;
import com.aveadvance.advancedachievements.data.services.UserTaskCategoryService;

@Controller
public class UserTaskController {
	
	@Autowired
	private UserTaskService userTaskService;
	
	@Autowired
	private UserTaskCategoryService userTaskCategoryService;
	
	/* TODO: What if user insert friends workspaceId? */
	@RequestMapping("/personal-tasks-page")
	@Secured("hasRole(ROLE_USER)")
	public String personalTasksPage(HttpServletRequest request, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		long workspaceId = (Long)request.getSession().getAttribute("workspaceId");
		Map<Optional<UserTaskCategory>,List<UserTask>> personalTasks = userTaskService.retrieve(auth.getName(), workspaceId)
				.parallelStream()
				.collect(Collectors.groupingBy(UserTask::getCategory));
		Map<Optional<UserTaskCategory>,List<UserTask>> allCategories = userTaskCategoryService.retrieveAll(workspaceId)
				.parallelStream()
				.filter(category -> !personalTasks.containsKey(Optional.ofNullable(category)))
				.collect(Collectors.toMap(category -> Optional.ofNullable(category), category -> new ArrayList<UserTask>()));
		personalTasks.putAll(allCategories);
		model.addAttribute("personalTasks", personalTasks);
		return "advance-achievements-user-task/personal-tasks-page";
	}
	
	@RequestMapping("/create-task-page")
	public String createTaskPage() {
		return "advance-achievements-user-task/create-task-page";
	}
	
	@RequestMapping("/create-task-page/{userTaskCategoryId}")
	public String createTaskInCategoryPage(@PathVariable long userTaskCategoryId, Model model) {
		model.addAttribute("userTaskCategoryId", userTaskCategoryId);
		return "advance-achievements-user-task/create-task-page";
	}

	@RequestMapping("/newtask")
	public String newTask(HttpServletRequest request, @Valid UserTaskDto userTaskDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return createTaskPage();
		}
		
		long workspaceId = (Long) request.getSession().getAttribute("workspaceId");
		
		if (userTaskDto.getUserTaskCategoryId() == 0)
			userTaskService.create(workspaceId, userTaskDto.getTitle(), userTaskDto.getDescription()
					, userTaskDto.getPriority());
		else
			userTaskService.create(workspaceId, userTaskDto.getTitle(), userTaskDto.getDescription()
					, userTaskDto.getPriority(), userTaskDto.getUserTaskCategoryId());
		return "redirect:/personal-tasks-page";
	}

	@RequestMapping("/deleteusertask")
	public String delete(long workspaceId, long id) {
		
		if (workspaceId < 1 || id < 1) {
			return "redirect:/" + workspaceId + "/personal-tasks-page";
		}
		
		userTaskService.delete(workspaceId, id);
		
		return "redirect:/personal-tasks-page";
	}

	@RequestMapping("/updateusertask")
	public String update(HttpServletRequest request, @Valid UserTaskDto userTaskDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "redirect:/personal-tasks-page";
		}
		
		long workspaceId = (Long) request.getSession().getAttribute("workspaceId");
		
		userTaskService.update(workspaceId, userTaskDto.getId(), userTaskDto.getTitle(), userTaskDto.getDescription(), userTaskDto.getPriority());
		
		return "redirect:/personal-tasks-page";
	}

}
