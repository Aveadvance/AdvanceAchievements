package com.advanceachievements.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import com.advanceachievements.data.dto.UserAccountDto;
import com.aveadvance.advancedachievements.data.services.UserAccountService;

@Controller
public class UserAccountController {
	
	@Autowired
	private UserAccountService userAccountService;
	
	@RequestMapping("/newaccount")
	public String newAccount(@Validated UserAccountDto userAccountDto, BindingResult bindingResult, Model model) {
		
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("exceptions", bindingResult);
			return createAccountPage();
		}
		
		boolean result = userAccountService.create(userAccountDto.getEmail(), userAccountDto.getPassword());
		if (!result) {
			bindingResult.addError(new FieldError("userAccountDto", "email", "User exists"));
			model.addAttribute("exceptions", bindingResult);
			return createAccountPage();
		}
		
		return "redirect:/";
	}
	
	@RequestMapping("/create-account")
	public String createAccountPage() {
		return "advance-achievements-user-account/create-account-form";
	}

}
