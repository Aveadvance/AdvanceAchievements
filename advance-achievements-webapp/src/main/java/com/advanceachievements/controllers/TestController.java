package com.advanceachievements.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
	
	@RequestMapping(path="/")
	public String testInit() {
		return "init";
	}

}
