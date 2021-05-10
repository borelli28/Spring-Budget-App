package com.armando.myBudget.controllers;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.armando.myBudget.models.User;
import com.armando.myBudget.services.UserService;
import com.armando.myBudget.validator.UserValidator;

@Controller
public class UserController {
	
    private UserService userService;
    
    private UserValidator userValidator;
    
    public UserController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @RequestMapping("/registration")
    public String registerForm(@Valid @ModelAttribute("user") User user) {
        return "user/registrationPage.jsp";
    }
    
    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute("user") User user, BindingResult result, Model model, HttpSession session) {
    	// check for null values in User
    	if (user.getFirstName().contains("null") || user.getLastName().contains("null") || user.getEmail().contains("null") || user.getPassword().contains("null")) {
    		System.out.println("Null values found... redirecting back to registration page");
    		ObjectError error = new ObjectError("firstName", "<null> values are not allowed");
    		result.addError(error);
    		return "user/registrationPage.jsp";
    	}
    	userValidator.validate(user, result);
    	if (result.hasErrors()) {
            return "user/registrationPage.jsp";
        } else {
            userService.saveUserWithAdminRole(user);
            return "redirect:/login";
        }
    }
    
    @RequestMapping("/login")
    public String login(
    		@RequestParam(value="error", required=false) String error, 
    		@RequestParam(value="logout", required=false) String logout,
    		Model model) {
        if(error != null && error != "") {
            model.addAttribute("errorMessage", "Invalid Credentials, Please try again.");
        }
        if(logout != null) {
            model.addAttribute("logoutMessage", "Logout Successful!");
        }
        return "user/loginPage.jsp";
    }
    
    @RequestMapping("/admin")
    public String adminPage(Principal principal, Model model) {
        String email = principal.getName();
        User user = userService.findByEmail(email);
        
        // decrypt first and last name of the user
        userService.decryptUser(user);
        model.addAttribute("currentUser", user);
        return "home/adminPage.jsp";
    }
    
    @RequestMapping(value = {"/", "/home"})
    public String home(Principal principal, Model model, HttpSession session) {
        String email = principal.getName();
        User user = userService.findByEmail(email);
        
        // First and Last name of the User is being decrypted so we can display it in the home page
        userService.decryptUser(user);
        model.addAttribute("currentUser", user);
        session.setAttribute("loggedUser", user);
        return "home/homePage.jsp";
    }
    
    // ACCOUNT URLS
    
    @RequestMapping("/account")
    public String account(Model model, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        model.addAttribute("user", loggedUser);
        
        // get user account
//        List<CashAcct> userAccounts = user.getCashAccts();
//        model.addAttribute("userAccounts", userAccounts);
    	
        return "account/account.jsp";
    }
    
    @RequestMapping("/account/chn-name")
    public String changeUserName(Model model, HttpSession session) {
    	// grabs the errors from session if the exist
    	if (session.getAttribute("userNameErrors") != null) {
    		model.addAttribute("userNameErrors", session.getAttribute("userNameErrors"));
    	}
    	
    	// create empty user instance for the form
    	User user = new User();
    	// put the empty user instance in the form
    	model.addAttribute("user", user);
    	return "account/changeName.jsp";
    }
    
    @RequestMapping(value="/account/chn-name", method=RequestMethod.PUT)
    public String changeUserName(@Valid @ModelAttribute("user") User user, BindingResult result, 
    		Model model,
    		HttpSession session) {
//		 clear the errors from session
		session.removeAttribute("userNameErrors");
    	
    	// rename the form user instance
    	User newUser = user;
    	// gets the current logged user so we can pass it to the service
    	// and update the user info
    	User loggedUser = (User) session.getAttribute("loggedUser");
    	
    	if (result.hasErrors()) {
    		System.out.println("Errors found while editing User name");
    		// gets all errors and save them into session so we can pass it into the template
    		List<FieldError> errors = result.getFieldErrors();
    		session.setAttribute("userNameErrors", errors);
  
    		return "redirect:/account/chn-name";
    	} else  {
    		userService.encryptAndSaveUser(newUser, loggedUser);
    		return "redirect:/home";
    	}
    }
    
    @RequestMapping("/account/chn-email")
    public String changeUserEmail(Model model, HttpSession session) {
    	// create empty user instance for the form
    	User user = new User();
    	// put the empty user instance in the form
    	model.addAttribute("user", user);
    	return "account/changeEmail.jsp";
    }
	
}
