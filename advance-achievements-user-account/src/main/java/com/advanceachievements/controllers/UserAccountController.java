package com.advanceachievements.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import com.advanceachievements.data.dto.UserAccountDto;
import com.advanceachievements.data.services.UserAccountService;

@Controller
public class UserAccountController {
	
	@Autowired
	private UserAccountService userAccountService;
	
	@RequestMapping("/newaccount")
	public String newAccount(@Validated UserAccountDto userAccountDto, BindingResult bindingResult) {
		if (!bindingResult.hasErrors()) {
			userAccountService.create(userAccountDto.getEmail(), userAccountDto.getPassword());
		}
		return "init.jsp";
	}

}
