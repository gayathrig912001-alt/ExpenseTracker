package com.example.Learning.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExpenseController {
	
	@GetMapping("/api/expense")
	public String getexpense() {
		return "Welcome to learn Spring security and build expense tracker";
	}

}
