package com.aveadvance.advancedachievements.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aveadvance.advancedachievements.data.dto.ResponseDto;
import com.aveadvance.advancedachievements.data.dto.UserTaskTimerDto;
import com.aveadvance.advancedachievements.data.services.UserTaskTimerService;

@RestController
public class UserTaskTimerRestController {
	
	@Autowired
	private UserTaskTimerService userTaskTimerService;
	
	@RequestMapping("/engagetasktimer/{id}")
	public @ResponseBody ResponseDto<UserTaskTimerDto> engageTaskTimer(@PathVariable long id) {
		return userTaskTimerService.engage(id);
	}

}
