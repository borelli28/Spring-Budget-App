package com.armando.myBudget.controllers;

import java.security.Principal;
import java.util.ArrayList;
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
import com.armando.myBudget.models.DueDate;
import com.armando.myBudget.models.Expense;
import com.armando.myBudget.models.Income;
import com.armando.myBudget.models.User;
import com.armando.myBudget.repositories.CashAcctRepo;
import com.armando.myBudget.repositories.DueDateRepo;
import com.armando.myBudget.repositories.ExpenseRepo;
import com.armando.myBudget.repositories.IncomeRepo;
import com.armando.myBudget.services.CashAccountService;
import com.armando.myBudget.services.DueDateService;
import com.armando.myBudget.services.ExpenseService;
import com.armando.myBudget.services.IncomeService;
import com.armando.myBudget.services.UserService;
import com.armando.myBudget.validator.UserValidator;

@Controller
public class MainController {
	
    private UserService userService;
    private CashAccountService cashAcctService;
    private UserValidator userValidator;
    private CashAcctRepo cashAcctRepo;
    private ExpenseService expenseService;
    private ExpenseRepo expenseRepo;
    private DueDateService duedateService;
    private IncomeService incomeService;
    private IncomeRepo incomeRepo;
    private DueDateRepo duedateRepo;
    
    public MainController(UserService userService, UserValidator userValidator, CashAccountService cashAcctService, 
    		CashAcctRepo cashAcctRepo, ExpenseService expenseService,
    		ExpenseRepo expenseRepo, DueDateService duedateService,
    		IncomeService incomeService, IncomeRepo incomeRepo,
    		DueDateRepo duedateRepo) {
    	
        this.userService = userService;
        this.userValidator = userValidator;
        this.cashAcctService = cashAcctService;
        this.cashAcctRepo = cashAcctRepo;
        this.expenseService = expenseService;
        this.expenseRepo = expenseRepo;
        this.duedateService = duedateService;
        this.incomeService = incomeService;
        this.incomeRepo = incomeRepo;
        this.duedateRepo = duedateRepo;
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
            return "user/loginPage.jsp";
        }
        
        if(logout != null) {
            model.addAttribute("logoutMessage", "Logout Successful!");
        }
        return "user/loginPage.jsp";
    }
   
    
//    // Home/Admin Methods
//    @RequestMapping("/admin")
//    public String adminPage(Principal principal, Model model) {
//    	
//        String email = principal.getName();
//        User user = userService.findByEmail(email);
//        
//        // decrypt first and last name of the user
//        userService.decryptUser(user);
//        model.addAttribute("currentUser", user);
//        return "home/adminPage.jsp";
//    }
    
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
        
        // get all the expenses of the user
        List<Expense> expensesEncrypted = user.getExpenses();
        
        // decrypt expenses
        List<Expense> expenses = expenseService.decryptExpenses(expensesEncrypted);
        // decrypt expense list of duedates and set the decrypted list as the due dates
        for (int i=0; i < expenses.size(); i++) {
        	Expense exp = expenses.get(i);
        	// send the list of expense due dates to decrypt is there is at least one element inside list
        	List<DueDate> decryptedDates = new ArrayList<DueDate>();
        	if(exp.getDueDates().size() > 0) {
        		decryptedDates.addAll(duedateService.decryptDuedates(exp.getDueDates()));
        		exp.setDueDates(decryptedDates);
        	}
        }
        
        model.addAttribute("expenses", expenses);
        session.setAttribute("expenses", expenses);
        
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
        
        // get user incomes
        List<Income> incomesEncrypted = user.getIncomes();
        // decrypt incomes
        List<Income> incomes = incomeService.decryptIncomes(incomesEncrypted);
        session.setAttribute("incomes", incomes);
        
        // calculate Free to Spend by getting all incomes total and then subtracting the total of all user expenses
        Double incomeTotal = 0.00;
        Double expensesTotal = 0.00;
        Double freeToSpendMonth = 0.00;
        for (int i=0; i < incomes.size(); i++) {
        	Double amount = Double.parseDouble(incomes.get(i).getAmount());
        	incomeTotal += amount;
        }
        for (int i=0; i < expenses.size(); i++) {
        	// get the duedates of each expense and multiply the expenses amount by the total of dates
        	// that way we are getting total monthly expense amount
        	Expense expense = expenses.get(i);
        	List<DueDate> duedates = expense.getDueDates();
        	int num = duedates.size();
        	Double expTotal = Double.parseDouble(expense.getAmount());
        	expensesTotal += (expTotal * num);
        }
        freeToSpendMonth = incomeTotal - expensesTotal;
        model.addAttribute("freeToSpendMonth", freeToSpendMonth);
        
        return "home/homePage.jsp";
   
    }
    
    //
    // USER ACCOUNT METHODS
    //
    
    @RequestMapping("/account")
    public String account(Model model, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        model.addAttribute("user", loggedUser);
        
        // reset all validations errors from the edit forms( in case user uses <a>go back</a> link after failed submission)
        session.removeAttribute("userNameErrors");
        session.removeAttribute("userEmailErrors");
        session.removeAttribute("userPasswordErrors");
           
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
        
        return "/manage/cashAccount/viewCashAccounts.jsp";
    }
    
    // handles the post data from add new cash account form
    @RequestMapping(value="/new/cashAcct", method=RequestMethod.POST)
    public String createCashAcct(@Valid @ModelAttribute("cashacct") CashAcct cashacct, BindingResult result, 
    		Model model,
    		HttpSession session) {

    	if (result.hasErrors()) {	
    		System.out.println("Errors found while creating new cash account");
  
    		return "/manage/cashAccount/viewCashAccounts.jsp";
    	}
    	
    	//  send the cash account to validate in the service
    	List<String> validationErrors =  cashAcctService.validateAccount(cashacct);
    	//  if there's is no errors then redirect to home. Else save the errors in model and render back the page
    	if (validationErrors.isEmpty()) {
    		cashAcctService.createSaveAcct(cashacct);
    		return "redirect:/home";
    	} else {
    		model.addAttribute("errors", validationErrors);
    		return "/manage/cashAccount/viewCashAccounts.jsp";
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
    	
    	return "/manage/cashAccount/editCashAccount.jsp";
    }
    
    // handles put form to edit cash account
    @RequestMapping(value="/edit/cashAcct/{accountId}", method=RequestMethod.PUT)
    public String editCashAccount(@Valid @ModelAttribute("cashAcct") CashAcct cashAcct, BindingResult result, @PathVariable("accountId") Long accountId, Model model, HttpSession session) {

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

        // get decrypted user expenses from session and save it in model
        model.addAttribute("expenses", session.getAttribute("expenses"));
        
        // for the new expense form
        Expense expense = new Expense();
        model.addAttribute("expense", expense);
        
        List<Expense> the = (List<Expense>) session.getAttribute("expenses");
        for (int i=0; i < the.size(); i++) {
        	if (the.get(i).getDueDates().size() > 0) {
        		Expense element = the.get(i);
        	}
        }
    	
    	return "manage/expenses/viewExpenses.jsp";
    }
    
    // handles the post data from add new expense form
    @RequestMapping(value="/new/expense", method=RequestMethod.POST)
    public String createExpense(@Valid @ModelAttribute("expense") Expense expense, BindingResult result, 
    		Model model,
    		HttpSession session) {
    	
    	//  send the expense to validate in the service
    	List<String> validationErrors =  expenseService.validateExpense(expense);
    	//  if there's is no errors then create new object and redirect to home. Else save the errors in model and render back the page
    	if (validationErrors.isEmpty()) {
    		expenseService.createSaveExpense(expense);
    		return "redirect:/home";
    	} else {
    		// view expenses page need decrypted list of expenses to display data
    		model.addAttribute("expenses", session.getAttribute("expenses"));
    		
    		model.addAttribute("errors", validationErrors);
    		return "manage/expenses/viewExpenses.jsp";
    	}
    }
    
    // renders the edit form for expenses
    @RequestMapping("/edit/expenses/{expId}")
    public String editExpenseForm(@PathVariable("expId") Long expId, Model model, HttpSession session) {
    	
    	// get errors from put method
    	model.addAttribute("errors", session.getAttribute("expensePutErrors"));
    			
    	// get the expense using the ID and then decrypt the account
    	Optional<Expense> iexpense = expenseRepo.findById(expId);
    	Expense expense = new Expense();
    	
    	// get the expense instance out of the Optional iexpense
    	if (iexpense.isPresent()) {
    		expense = iexpense.get();
    	} else {
    		return "redirect:/home";
    	}
    	
    	Expense theExpense = expenseService.decryptExpense(expense);
    	model.addAttribute("expense", theExpense);
    	
    	User user = (User) session.getAttribute("loggedUser");
    	model.addAttribute("user", user);
    	
    	// pass an empty due date object for adding due date for expenses
    	DueDate duedate = new DueDate();
    	model.addAttribute("duedate", duedate);
    	
    	// pass errors from duedate form
    	model.addAttribute("duedateErrors", session.getAttribute("duedateErrors"));
    	
    	return "/manage/expenses/editExpense.jsp";
    }
    
    // handles put form data to edit expense
    @RequestMapping(value="/edit/expenses/{expId}", method=RequestMethod.PUT)
    public String editCashAccount(@Valid @ModelAttribute("expense") Expense expense, BindingResult result, 
    		@PathVariable("expId") Long expId, Model model, HttpSession session) {

    	//reset errors in session
    	session.removeAttribute("expensePutErrors");
    	
    	//  send the expense object to validate in the service
    	List<String> validationErrors =  expenseService.validateExpense(expense);
    	//  if there's is no errors then redirect to home. Else save the errors in model and render back the page
    	if (validationErrors.isEmpty()) {
    		expenseService.updateExpense(expense, expId);
    		return "redirect:/home";
    	} else {
    		System.out.println("Errors found when validating Expense");
    		for (int i=0; i < validationErrors.size(); i++) {
    			System.out.println(validationErrors.get(i));
    		}
    		session.setAttribute("expensePutErrors", validationErrors);
    		return "redirect:/edit/expenses/" + expId;
    	}
    }
    
    // handles the delete data to delete the expense
    @RequestMapping(value="/delete/expenses/{expId}", method=RequestMethod.DELETE)
    public String deleteExpense(@PathVariable("expId") Long expId, Model model, HttpSession session) {
		System.out.println("Inside deleteExpense()");
		Optional<Expense> expOpt = expenseRepo.findById(expId);
		Expense exp = new Expense();
		if (expOpt.isPresent()) {
			exp = expOpt.get();
		}
		
		List<DueDate> duedates = exp.getDueDates();
		
    	expenseService.deleteExpense(expId, duedates);
    	System.out.println("Expense deleted");
    	return "redirect:/home";
    }
    
    
    //
    // DUE DATE'S METHODS
    //
    
    // handles to data from the assign due date form in editExpense.jsp
    @RequestMapping(value="/add/duedate/{expId}", method=RequestMethod.POST)
    public String createDueDate(@Valid @ModelAttribute("duedate") DueDate duedate, BindingResult result,
    		@PathVariable("expId") Long expId, HttpSession session) {

		// reset errors
		session.removeAttribute("duedateErrors");
		
		if (result.hasErrors()) {
			return "redirect:/edit/expenses/" + expId;
		}
		
		// validate DueDate object and return a list of errors if any
		List<String> errors = duedateService.validateDueDate(duedate);
		
		
    	if (!errors.isEmpty()) {
    		System.out.println("Validations error found while creating duedate");
			session.setAttribute("duedateErrors", errors);
			return "redirect:/edit/expenses/" + expId;
    	} else {
    		duedateService.createDuedate(duedate);
    		System.out.println("due date created");
    	}
    	return "redirect:/home";
    }
    
    // renders the edit due date page
    @RequestMapping("/duedate/{duedateId}/{expId}")
    public String editDuedateForm(@PathVariable("duedateId") Long duedateId, @PathVariable("expId") Long expId, 
    		Model model, HttpSession session) {
    	
    	// get duedate object
    	Optional<DueDate> duedateOpt = duedateRepo.findById(duedateId);
    	DueDate duedate = new DueDate();
    	if (duedateOpt.isPresent()) {
    		duedate = duedateOpt.get();
    	}
    	
    	// get expense objet
    	Optional<Expense> expOpt = expenseRepo.findById(expId);
    	Expense exp = new Expense();
    	if (expOpt.isPresent()) {
    		exp = expOpt.get();
    	}

    	model.addAttribute("exp", expenseService.decryptExpense(exp));
    	model.addAttribute("duedate", duedateService.decryptDueDate(duedate));

    	return "manage/duedate/duedate.jsp";
    }
    
    // delete the duedate
    @RequestMapping(value="/delete/duedate/{duedateId}", method=RequestMethod.DELETE)
    public String deleteDuedate(@PathVariable("duedateId") Long duedateId, Model model, HttpSession session) {
		
    	duedateService.deleteDuedate(duedateId);
    	System.out.println("Duedate deleted");
    	return "redirect:/home";
    }
    
    
    //
    // INCOME
    //
    
    
    // renders income view page
    @RequestMapping("/income")
    public String viewIncome(HttpSession session, Model model) {
    	
        User loggedUser = (User) session.getAttribute("loggedUser");
        model.addAttribute("user", loggedUser);

        // get all the decrypted incomes of the user from session
        model.addAttribute("incomes", session.getAttribute("incomes"));
        
        // for the new income form
        Income income = new Income();
        model.addAttribute("income", income);
        
        return "/manage/income/viewIncome.jsp";
    }
    
    // handles the post data from add new income form
    @RequestMapping(value="/new/income", method=RequestMethod.POST)
    public String createIncome(@Valid @ModelAttribute("income") Income income, BindingResult result, 
    		Model model,
    		HttpSession session) {

    	if (result.hasErrors()) {	
    		System.out.println("Errors found while creating new income");
  
    		return "/manage/income/viewIncome.jsp";
    	}
    	
    	//  send the income instance to validate in the service
    	List<String> validationErrors =  incomeService.validateIncome(income);
    	//  if there's is no errors then redirect to home. Else save the errors in model and render back the page
    	if (validationErrors.isEmpty()) {
    		incomeService.createSaveIncome(income);
    		return "redirect:/home";
    	} else {
    		model.addAttribute("errors", validationErrors);
    		return "/manage/income/viewIncome.jsp";
    	}
    }
    
    // renders the edit income form
    @RequestMapping("/edit/income/{incomeId}")
    public String editIncomeForm(@PathVariable("incomeId") Long incomeId, Model model, HttpSession session) {
    	
    	// get errors from put method
    	model.addAttribute("errors", session.getAttribute("incomePutErrors"));
    			
    	// get the income using the ID and then decrypt the account
    	Optional<Income> incomeOpt = incomeRepo.findById(incomeId);
    	Income incomeEncrypted = new Income();
    	
    	if (incomeOpt.isPresent()) {
    		incomeEncrypted = incomeOpt.get();
    	} else {
    		return "redirect:/home";
    	}
    	
    	Income income = incomeService.decryptIncome(incomeEncrypted);
    	model.addAttribute("income", income);
    	
    	User user = (User) session.getAttribute("loggedUser");
    	model.addAttribute("user", user);
    	
    	return "/manage/income/editIncome.jsp";
    }
    
    // handles put form to edit income
    @RequestMapping(value="/edit/income/{incomeId}", method=RequestMethod.PUT)
    public String editIncome(@Valid @ModelAttribute("income") Income income, BindingResult result, 
    		@PathVariable("incomeId") Long incomeId, Model model, HttpSession session) {

    	//reset errors in session
    	session.removeAttribute("incomePutErrors");
    	
    	//  send the income to validate in the service
    	List<String> validationErrors =  incomeService.validateIncome(income);
    	//  if there's is no errors then redirect to home. Else save the errors in model and render back the page
    	if (validationErrors.isEmpty()) {
    		incomeService.updateIncome(income, incomeId);
    		return "redirect:/home";
    	} else {
    		System.out.println("Errors found when validating Income");

    		session.setAttribute("incomePutErrors", validationErrors);
    		return "redirect:/edit/income/" + incomeId;
    	}
    	
    }
    
    // delete income
    @RequestMapping(value="/delete/income/{incomeId}", method=RequestMethod.DELETE)
    public String deleteIncome(@PathVariable("incomeId") Long incomeId, Model model, HttpSession session) {
    	incomeService.deleteIncome(incomeId);
    	System.out.println("income deleted");
    	return "redirect:/home";
    }
    
}
