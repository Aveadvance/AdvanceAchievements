package com.advanceachievements.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserAccountController {
	
	@RequestMapping("/newaccount")
	public String newAccount(String email, String password) {
		System.out.println(email);
		System.out.println(password);
		return "init.jsp";
	}

}
