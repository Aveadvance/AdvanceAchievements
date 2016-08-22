package com.advanceachievements.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import com.advanceachievements.data.dto.UserAccountDto;

@Controller
public class UserAccountController {
	
	@RequestMapping("/newaccount")
	public String newAccount(@Validated UserAccountDto userAccountDto, BindingResult bindingResult) {
		System.out.println(userAccountDto.getEmail());
		System.out.println(userAccountDto.getPassword());
		for (ObjectError er : bindingResult.getAllErrors()) {
			System.out.println(er.getCode()+" " + er.getDefaultMessage());
		}
		return "init.jsp";
	}

}
