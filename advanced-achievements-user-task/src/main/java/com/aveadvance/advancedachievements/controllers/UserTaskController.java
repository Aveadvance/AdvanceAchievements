package com.aveadvance.advancedachievements.controllers;

import java.time.LocalDateTime;
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

import com.aveadvance.advancedachievements.data.dto.UserTaskDto;
import com.aveadvance.advancedachievements.data.entities.UserTask;
import com.aveadvance.advancedachievements.data.entities.UserTaskCategory;
import com.aveadvance.advancedachievements.data.entities.UserTaskState;
import com.aveadvance.advancedachievements.data.services.UserTaskCategoryService;
import com.aveadvance.advancedachievements.data.services.UserTaskService;
import com.aveadvance.advancedachievements.exceptions.ExceptionsDto;

@Controller
public class UserTaskController {
	
	@Autowired
	private UserTaskService userTaskService;
	
	@Autowired
	private UserTaskCategoryService userTaskCategoryService;
	
	/* TODO: What if user insert friends workspaceId? */
	@RequestMapping("/personal-tasks-page")
	@Secured("hasRole(ROLE_USER)")
	public String personalTasksPage(String state, String completed, String created, HttpServletRequest request, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		UserTaskState taskState = UserTaskState.TO_DO;
		Optional<LocalDateTime> createdSince = Optional.empty();
		Optional<LocalDateTime> createdTill = Optional.empty();
		Optional<LocalDateTime> completedSince = Optional.empty();
		Optional<LocalDateTime> completedTill = Optional.empty();
		
		LocalDateTime now = LocalDateTime.now();
		
		if (state != null)
			switch (state) {
				case "completed":
					taskState = UserTaskState.ACHIEVED;
					break;
			}
		
		if (created != null)
			switch (created) {
				case "today":
					createdSince = Optional.of(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0));
					createdTill = Optional.of(now);
					break;
				case "yesterday":
					createdSince = Optional.of(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0).minusDays(1));
					createdTill = Optional.of(now);
					break;
			}
		
		if (completed != null)
			switch (completed) {
				case "today":
					completedSince = Optional.of(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0));
					completedTill = Optional.of(now);
					break;
				case "yesterday":
					completedSince = Optional.of(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0).minusDays(1));
					completedTill = Optional.of(now);
					break;
			}
		
		final Optional<LocalDateTime> createdSinceF = createdSince;
		final Optional<LocalDateTime> createdTillF = createdTill;
		final Optional<LocalDateTime> completedSinceF = completedSince;
		final Optional<LocalDateTime> completedTillF = completedTill;
		
		long workspaceId = (Long)request.getSession().getAttribute("workspaceId");
		
		/* Retrieve tasks and collect them to map. */
		Map<Optional<UserTaskCategory>,List<UserTask>> personalTasks = userTaskService.retrieve(auth.getName(), workspaceId, taskState)
				.parallelStream()
				.filter(task -> {
					/* TODO: Move filtering to client side. */
					
					if (completedSinceF.isPresent() && completedTillF.isPresent()) {
						return task.getCompletionDate().isAfter(completedSinceF.get()) 
								&& task.getCompletionDate().isBefore(completedTillF.get());
					} else if (createdSinceF.isPresent() && createdTillF.isPresent()) {
						return task.getCreationDate().isAfter(createdSinceF.get())
								&& task.getCreationDate().isBefore(createdTillF.get());
					}
					
					return true;
				})
				.collect(Collectors.groupingBy(UserTask::getCategory));
		
		/* Retrieve empty task categories. */
		Map<Optional<UserTaskCategory>,List<UserTask>> allCategories = userTaskCategoryService.retrieveAll(workspaceId)
				.parallelStream()
				.filter(category -> !personalTasks.containsKey(Optional.ofNullable(category)))
				.collect(Collectors.toMap(category -> Optional.ofNullable(category), category -> new ArrayList<UserTask>()));
		
		/* Put empty categories to list. */
		personalTasks.putAll(allCategories);
		
		/* Sort tasks by priority and date. */
		personalTasks.entrySet().forEach(action -> action.getValue().sort((e1, e2) -> {
			int firstCompare = e1.getPriority().compareTo(e2.getPriority());
			if (firstCompare != 0) {
				return firstCompare;
			} else {
				return e1.getCreationDate().compareTo(e2.getCreationDate());
			}
		}));
		
		model.addAttribute("personalTasks", personalTasks);
		
		Optional.ofNullable((ExceptionsDto)request.getSession().getAttribute("exceptionsDto")).ifPresent(exceptionsDto -> {
			model.addAttribute("exceptionsDto", exceptionsDto);
			request.getSession().removeAttribute("exceptionsDto");
		});
		
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
	
	@RequestMapping("/update-task-page")
	public String updateTaskPage(long id, HttpServletRequest request, Model model) {
		long workspaceId = (Long) request.getSession().getAttribute("workspaceId");
		Optional<UserTask> userTaskToUpdate = userTaskService.retrieve(workspaceId, id);
		if(userTaskToUpdate.isPresent()) {
			model.addAttribute("userTaskToUpdate", userTaskToUpdate.get());
			return createTaskPage();
		}
		ExceptionsDto exceptions = new ExceptionsDto();
		exceptions.addException("global", "Data do not match. Try again please.");
		request.getSession().setAttribute("exceptionsDto", exceptions);
		return "redirect:/personal-tasks-page";
	}
	
	@RequestMapping("/complete-task")
	public String updateTaskPage(long id, HttpServletRequest request) {
		long workspaceId = (Long) request.getSession().getAttribute("workspaceId");
		userTaskService.completeTask(workspaceId, id);
		return "redirect:/personal-tasks-page";
	}

	@RequestMapping("/newtask")
	public String newTask(HttpServletRequest request, @Valid UserTaskDto userTaskDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
//			for (ObjectError er : bindingResult.getAllErrors()) {
//				System.out.println(er.getDefaultMessage());
//			}
			return createTaskPage();
		}
		
		long workspaceId = (Long) request.getSession().getAttribute("workspaceId");
		
		if (userTaskDto.getId() == 0) {
			if (userTaskDto.getUserTaskCategoryId() == 0)
				userTaskService.create(workspaceId, userTaskDto.getTitle(), userTaskDto.getDescription()
						, userTaskDto.getPriority());
			else
				userTaskService.create(workspaceId, userTaskDto.getTitle(), userTaskDto.getDescription()
						, userTaskDto.getPriority(), userTaskDto.getUserTaskCategoryId());
		} else {
			userTaskService.update(workspaceId, userTaskDto.getId(), userTaskDto.getTitle()
					, userTaskDto.getDescription(), userTaskDto.getPriority());
		}
		
		return "redirect:/personal-tasks-page";
	}

	@RequestMapping("/deleteusertask")
	public String delete(long id, HttpServletRequest request) {
		long workspaceId = (Long)request.getSession().getAttribute("workspaceId");
		
		if (workspaceId < 1 || id < 1) {
			return "redirect:/personal-tasks-page";
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
