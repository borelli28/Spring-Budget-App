package com.armando.myBudget.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.armando.myBudget.models.CashAcct;
import com.armando.myBudget.models.Expense;
import com.armando.myBudget.models.User;
import com.armando.myBudget.repositories.CashAcctRepo;
import com.armando.myBudget.services.CashAccountService;
import com.armando.myBudget.services.ExpenseService;
import com.armando.myBudget.services.UserService;
import com.armando.myBudget.validator.UserValidator;

@Controller
public class MainController {
	
    private UserService userService;
    private CashAccountService cashAcctService;
    private UserValidator userValidator;
    private CashAcctRepo cashAcctRepo;
    private ExpenseService expenseService;
    
    public MainController(UserService userService, UserValidator userValidator, CashAccountService cashAcctService, CashAcctRepo cashAcctRepo, ExpenseService expenseService) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.cashAcctService = cashAcctService;
        this.cashAcctRepo = cashAcctRepo;
        this.expenseService = expenseService;
    }
    
    
    //
    // BASIC METHODS
    //
    
    
    // Registration and Login Methods
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
    
    // Home/Admin Methods
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

    	// testing other controller delete this later
    	session.setAttribute("test", "This text comes from Main Controller!");
    	
        String email = principal.getName();
        User user = userService.findByEmail(email);
        
        // First and Last name of the User is being decrypted so we can display it in the home page
        userService.decryptUser(user);
        model.addAttribute("currentUser", user);
        session.setAttribute("loggedUser", user);
        
        // decrypt Cash accounts data and save it in session
        List<CashAcct> userCashAccts = user.getCashAccts();
        cashAcctService.decryptCashAccts(userCashAccts);
        session.setAttribute("userCashAccts", userCashAccts);
        
        session.setAttribute("userCashAccts", userCashAccts);
        
        // get the user balance by adding all accounts amounts
        Double userBalance = 0.00;
        for (int i=0; i < userCashAccts.size(); i++) {
        	// get the amount and added to the userBalance
        	CashAcct account = userCashAccts.get(i);
        	// check that account.getAmonut() is not empty before parsing else an error will appear
        	if (!account.getAmount().isEmpty()) {
            	// convert string amount into a number
            	Double amount = Double.parseDouble(account.getAmount());
            	userBalance += amount;
        	} else {
        		// amount is an empty string for some reason so
        		// we are going to assign the value null to user Balance
        		System.out.println("Cash Account amount is empty. Assigning null to User Balance");
        		userBalance = null;
        	}
        }
        model.addAttribute("userBalance", userBalance);
        
        return "home/homePage.jsp";
   
    }
    
    //
    // USER ACCOUNT METHODS
    //
    
    @RequestMapping("/account")
    public String account(Model model, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        model.addAttribute("user", loggedUser);
           
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
    		// gets all errors and save them into session so we can pass it into the template
    		List<FieldError> errors = result.getFieldErrors();
    		session.setAttribute("userNameErrors", errors);
  
    		return "redirect:/account/chn-name";
    	} else  {
    		// we pass the newUser instance(with the first and last name that we updated
    		// and we pass the encrypted user instance
    		userService.updateUserName(newUser, userService.findByEmail(loggedUser.getEmail()));
    		return "redirect:/home";
    	}
    }
    
    @RequestMapping("/account/chn-email")
    public String changeUserEmail(Model model, HttpSession session) {
    	// grabs the errors from session if the exist
    	if (session.getAttribute("userEmailErrors") != null) {
    		model.addAttribute("userEmailErrors", session.getAttribute("userEmailErrors"));
    	}
    	
    	// create empty user instance for the form
    	User user = new User();
    	// put the empty user instance in the form
    	model.addAttribute("user", user);
    	return "account/changeEmail.jsp";
    }
    
    @RequestMapping(value="/account/chn-email", method=RequestMethod.PUT)
    public String changeUserEmail(@Valid @ModelAttribute("user") User user, BindingResult result, 
    		Model model,
    		HttpSession session,
    		Principal principal) {
		// clear the errors from session
		session.removeAttribute("userEmailErrors");
    	
    	// rename the form user instance
    	User newUser = user;
    	// gets the current logged user so we can pass it to the service
    	// and update the user info
    	User loggedUser = (User) session.getAttribute("loggedUser");
    	
    	if (result.hasErrors()) {
    		System.out.println("Errors found while editing User email");
    		// gets all errors and save them into session so we can pass it into the template
    		List<FieldError> errors = result.getFieldErrors();
    		session.setAttribute("userEmailErrors", errors);
  
    		return "redirect:/account/chn-email";
    	} else  {
    		// pass the new email that we want to save
    		// and also pass the logged user instance that is encrypted
    		// the one instance that we have in loggedUser was decrypted
    		userService.updateUserEmail(newUser.getEmail(), userService.findByEmail(loggedUser.getEmail()));
    		
    		// user will be log out after updating email
    		// the reason is that home use principal.getName() to get the current logged user email
    		// and there is no way of updating the principal name that I know of
    		return "redirect:/login";
    	}
    }
    
    @RequestMapping("/account/chn-password")
    public String changeUserPassword(Model model, HttpSession session) {
    	// grabs the errors from session if the exist
    	if (session.getAttribute("userPasswordErrors") != null) {
    		model.addAttribute("userPasswordErrors", session.getAttribute("userPasswordErrors"));
    	}
    	
    	// create empty user instance for the form
    	User user = new User();
    	// put the empty user instance in the form
    	model.addAttribute("user", user);
    	return "account/changePassword.jsp";
    }
    
    @RequestMapping(value="/account/chn-password", method=RequestMethod.PUT)
    public String changeUserPassword(@Valid @ModelAttribute("user") User user, BindingResult result, 
    		Model model,
    		HttpSession session,
    		Principal principal) {
		// clear the errors from session
		session.removeAttribute("userPasswordErrors");
    	
    	// rename the form user instance
    	User newUser = user;
    	// gets the current logged user so we can pass it to the service
    	// and update the user info
    	User loggedUser = (User) session.getAttribute("loggedUser");
    	
    	if (result.hasErrors()) {
    		System.out.println("Errors found while editing User Password");
    		// gets all errors and save them into session so we can pass it into the template
    		List<FieldError> errors = result.getFieldErrors();
    		session.setAttribute("userPasswordErrors", errors);
  
    		return "redirect:/account/chn-password";
    	} else  {
    		// pass the new password that we want to save
    		// and also pass the logged user instance that is encrypted
    		// the one instance that we have in loggedUser was decrypted
    		userService.updateUserPassword(newUser.getPassword(), userService.findByEmail(loggedUser.getEmail()));
    		
    		return "redirect:/home";
    	}
    }
    
    
    //
    // MANAGE DROPDOWN METHODS
    //
    
    
    // Cash Accounts Methods
    // renders view account page( with the add new account form)
    @RequestMapping("/cash-account-view")
    public String cashAccount(Model model, HttpSession session) {
    	
        User loggedUser = (User) session.getAttribute("loggedUser");
        model.addAttribute("user", loggedUser);

        // get all the accounts the user owns
        List<CashAcct> accounts = loggedUser.getCashAccts();
        model.addAttribute("accounts", accounts);
        
        // for the new account form
        CashAcct cashacct = new CashAcct();
        model.addAttribute("cashacct", cashacct);
        
        return "/edit/cashAccount/viewCashAccounts.jsp";
    }
    
    // handles the post data from add new cash account form
    @RequestMapping(value="/new/cashAcct", method=RequestMethod.POST)
    public String createCashAcct(@Valid @ModelAttribute("cashacct") CashAcct cashacct, BindingResult result, 
    		Model model,
    		HttpSession session) {
    	
		System.out.println("Inside createCashAcct()");

    	if (result.hasErrors()) {	
    		System.out.println("Errors found while creating new cash account");
  
    		return "/edit/cashAccount/viewCashAccounts.jsp";
    	}
    	
    	//  send the cash account to validate in the service
    	List<String> validationErrors =  cashAcctService.validateAccount(cashacct);
    	//  if there's is no errors then redirect to home. Else save the errors in model and render back the page
    	if (validationErrors.isEmpty()) {
    		cashAcctService.createSaveAcct(cashacct);
    		return "redirect:/home";
    	} else {
    		model.addAttribute("errors", validationErrors);
    		return "/edit/cashAccount/viewCashAccounts.jsp";
    	}
    }
    
    // renders the edit cash account form
    @RequestMapping("/edit/cashAcct/{accountId}")
    public String editCashAccountForm(@PathVariable("accountId") Long accountId, Model model, HttpSession session) {
    	
    	// get errors from put method
    	model.addAttribute("errors", session.getAttribute("cashAcctPutErrors"));
    			
    	// get the account using the ID and then decrypt the account
    	Optional<CashAcct> cashacct = cashAcctRepo.findById(accountId);
    	CashAcct cashAccount = new CashAcct();
    	
    	if (cashacct.isPresent()) {
    		cashAccount = cashacct.get();
    	} else {
    		return "redirect:/home";
    	}
    	
    	CashAcct cashAcct = cashAcctService.decryptCashAcct(cashAccount);
    	model.addAttribute("cashAcct", cashAcct);
    	
    	User user = (User) session.getAttribute("loggedUser");
    	model.addAttribute("user", user);
    	
    	return "/edit/cashAccount/editCashAccount.jsp";
    }
    
    // handles put form to edit cash account
    @RequestMapping(value="/edit/cashAcct/{accountId}", method=RequestMethod.PUT)
    public String editCashAccount(@Valid @ModelAttribute("cashAcct") CashAcct cashAcct, BindingResult result, @PathVariable("accountId") Long accountId, Model model, HttpSession session) {
    	
    	System.out.println("Inside edit cash account put method");
    	//reset errors in session
    	session.removeAttribute("cashAcctPutErrors");
    	
    	//  send the cash account to validate in the service
    	List<String> validationErrors =  cashAcctService.validateAccount(cashAcct);
    	//  if there's is no errors then redirect to home. Else save the errors in model and render back the page
    	if (validationErrors.isEmpty()) {
    		cashAcctService.updateCashAcct(cashAcct, accountId);
    		return "redirect:/home";
    	} else {
    		System.out.println("Errors found when validating Cash Account");
    		for (int i=0; i < validationErrors.size(); i++) {
    			System.out.println(validationErrors.get(i));
    		}
    		session.setAttribute("cashAcctPutErrors", validationErrors);
    		return "redirect:/edit/cashAccount/cashAcct/" + accountId;
    	}
    	
    }
    
    // handles the delete data to delete the cash account
    @RequestMapping(value="/delete/cashAcct/{accountId}", method=RequestMethod.DELETE)
    public String deleteCashAcct(@PathVariable("accountId") Long accountId, Model model, HttpSession session) {
		System.out.println("Inside deleteCashAcct()");
    	cashAcctService.deleteCashAcct(accountId);
    	System.out.println("Cash Account deleted");
    	return "redirect:/home";
    }
    
    // Expenses Methods
    // renders view my expenses page
    @RequestMapping("/expenses")
    public String expenses(HttpSession session, Model model) {
    	
        User loggedUser = (User) session.getAttribute("loggedUser");
        model.addAttribute("user", loggedUser);

        // get all the expenses of the user
        List<Expense> expensesEncrypted = loggedUser.getExpenses();
        
        // decrypt expenses
        List<Expense> expenses = expenseService.decryptExpenses(expensesEncrypted);
        model.addAttribute("expenses", expenses);
        
        // for the new expense form
        Expense expense = new Expense();
        model.addAttribute("expense", expense);
    	
    	return "manage/expenses/viewExpenses.jsp";
    }
    
    // handles the post data from add new expense form
    @RequestMapping(value="/new/expense", method=RequestMethod.POST)
    public String createExpense(@Valid @ModelAttribute("expense") Expense expense, BindingResult result, 
    		Model model,
    		HttpSession session) {
    	
		System.out.println("Inside createExpense()");

    	if (result.hasErrors()) {	
    		System.out.println("Errors found while creating new expense");
  
    		return "/manage/expenses/viewExpenses.jsp";
    	}
    	
    	//  send the expense to validate in the service
    	List<String> validationErrors =  expenseService.validateExpense(expense);
    	//  if there's is no errors then create new object and redirect to home. Else save the errors in model and render back the page
    	if (validationErrors.isEmpty()) {
    		expenseService.createSaveExpense(expense);
    		return "redirect:/home";
    	} else {
    		model.addAttribute("errors", validationErrors);
    		return "/manage/expenses/viewExpenses.jsp";
    	}
    }
    
}
