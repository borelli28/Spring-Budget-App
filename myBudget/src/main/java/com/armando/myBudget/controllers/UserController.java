package com.armando.myBudget.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.armando.myBudget.models.User;
import com.armando.myBudget.services.UserService;

@Controller
public class UserController {
	
    private UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/registration")
    public String registerForm(@Valid @ModelAttribute("user") User user) {
        return "registrationPage.jsp";
    }
    
    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute("user") User user, BindingResult result, Model model, HttpSession session) {
    	// check for null values in User
    	if (user.getFirstName().contains("null") || user.getLastName().contains("null") || user.getEmail().contains("null") || user.getPassword().contains("null")) {
    		System.out.println("Null values found... redirecting back to registration page");
    		ObjectError error = new ObjectError("firstName", "<null> values are not allowed");
    		result.addError(error);
    		return "registrationPage.jsp";
    	}
    	
    	if (result.hasErrors()) {
    		System.out.println("Errors found while registering user");
            return "registrationPage.jsp";
        } else {
        	System.out.println("First name(null) length:");
        	System.out.println(user.getFirstName().length());
            userService.saveWithUserRole(user);
            return "redirect:/login";
        }
    }
    
    @RequestMapping("/login")
    public String login() {
        return "loginPage.jsp";
    }
	
}
