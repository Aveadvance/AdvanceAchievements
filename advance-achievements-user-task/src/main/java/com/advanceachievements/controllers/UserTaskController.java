package com.advanceachievements.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.advanceachievements.data.entities.UserTaskDto;
import com.advanceachievements.data.services.UserTaskService;

@Controller
public class UserTaskController {
	
	@Autowired
	private UserTaskService userTaskService;

	@RequestMapping("/newtask")
	public String create(@Valid UserTaskDto userTaskDto, BindingResult bindingResult) {
		System.out.println(userTaskDto.getTitle());
		System.out.println(userTaskDto.getDescription());
		System.out.println(userTaskDto.getPriority());
		if (bindingResult.hasErrors()) {
			return "";
		}
		userTaskService.create(userTaskDto);
		return "";
	}

}
