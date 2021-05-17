package com.armando.myBudget.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.stereotype.Service;

import com.armando.myBudget.llaves.MyKeys;
import com.armando.myBudget.models.CashAcct;
import com.armando.myBudget.models.Expense;
import com.armando.myBudget.repositories.ExpenseRepo;

@Service
public class ExpenseService {

	private final ExpenseRepo expenseRepo;
    private final MyKeys myKeys;
    
    public ExpenseService(ExpenseRepo expenseRepo, MyKeys myKeys) {
    	this.expenseRepo = expenseRepo;
    	this.myKeys = myKeys;
    }
    
	public void createSaveExpense(Expense expense) {
		System.out.println("Inside createSaveExpense()");
		// encrypt title and amount of expense before saving to the DB
        AES256TextEncryptor aes256TextEncryptor = new AES256TextEncryptor();
        aes256TextEncryptor.setPassword(myKeys.getMelchor());
		
        String titlePlainText = expense.getTitle();        
        String amountPlainText = expense.getAmount();
        
    	String encryptedTitle = aes256TextEncryptor.encrypt(titlePlainText);
    	String encryptedAmount = aes256TextEncryptor.encrypt(amountPlainText);
    	
    	expense.setTitle(encryptedTitle);
    	expense.setAmount(encryptedAmount);
        
		expenseRepo.save(expense);
	}
	
	public void updateExpense(Expense expense, Long expId) {
		
		System.out.println("Inside updateExpense()");
		// get OG cash account instance that we are editing
		Optional<Expense> ogExp = expenseRepo.findById(expId);
		Expense editThisExpense = new Expense();
		if (ogExp.isPresent()) {
			editThisExpense = ogExp.get();
		}
		
		// encrypt title and amount of cash account before saving to the DB
        AES256TextEncryptor aes256TextEncryptor = new AES256TextEncryptor();
        aes256TextEncryptor.setPassword(myKeys.getMelchor());
		
        String titlePlainText = expense.getTitle();        
        String amountPlainText = expense.getAmount();
        
    	String encryptedTitle = aes256TextEncryptor.encrypt(titlePlainText);
    	String encryptedAmount = aes256TextEncryptor.encrypt(amountPlainText);
    	
    	editThisExpense.setTitle(encryptedTitle);
    	editThisExpense.setAmount(encryptedAmount);
        
		expenseRepo.save(editThisExpense);
	}
	
    // validate expenses attributes
    public List<String> validateExpense(Expense expense) {
    	
		String title = expense.getTitle();
		String amount = expense.getAmount();
    	
		// initalize errors list
		List<String> expenseErrors = new ArrayList();
		
		// Check that inputs are valid( Title - a string with at least 2 character not null)
    	if (title.length() > 1 == false || title == null) {
    		expenseErrors.add("Please enter a title");
    	}
    	
    	// Check that amount is not null
    	if (amount == null) {
    		expenseErrors.add("Please enter an amount");
	    } else {
	    	// Check that amount has two decimal points
	    	
    		// convert amount to string so we can validate that amount has two decimal points
    		String amountStr = String.valueOf(amount);
    		//assert that the string is valid number
    		int i = amountStr.lastIndexOf('.');
    		if(i != -1 && amountStr.substring(i + 1).length() == 2) {
    		    System.out.println("The amount " + amountStr + " has two digits after dot");
    		} else {
    			// amount does not have two decimal places so we pass the error message
    			expenseErrors.add("Please enter two decimal digits after the number. Example: $250.00");
    		}	
	    }
    	
    	return expenseErrors;
    }
    
	// decrypt List of expenses
	public List<Expense> decryptExpenses(List<Expense> expenses) {
		System.out.println("Inside decryptExpenses() in expenses service");
		// instanciate and pass password to the encryptors that were using
        AES256TextEncryptor aes256TextEncryptor = new AES256TextEncryptor();
        aes256TextEncryptor.setPassword(myKeys.getMelchor());
		
        List<Expense> decryptedexpenses = new ArrayList<Expense>();

		// iterate trough each expense and decrypt the attributes in there
		for (int i=0; i < expenses.size(); i++) {
			Expense exp = expenses.get(i);

			String decryptedTitle = aes256TextEncryptor.decrypt(exp.getTitle());
			String decryptedAmount = aes256TextEncryptor.decrypt(exp.getAmount());
			
			// create a copy of the expense because we dont want to decrypt the OG account in the DB
			Expense decryptedExpense = exp;
			decryptedExpense.setTitle(decryptedTitle);
			decryptedExpense.setAmount(decryptedAmount);
			
			// add the decrypted Expense to the list of the decrypted accounts
			decryptedexpenses.add(i, decryptedExpense);
		}
		System.out.println("Done decrypting all expenses info now returning the list");
		return decryptedexpenses;
	}
	
	// decrypt a expense object
	public Expense decryptExpense(Expense expense) {
		// instanciate and pass password to the encryptors that were using
        AES256TextEncryptor aes256TextEncryptor = new AES256TextEncryptor();
        aes256TextEncryptor.setPassword(myKeys.getMelchor());
			
		String decryptedTitle = aes256TextEncryptor.decrypt(expense.getTitle());
		String decryptedAmount = aes256TextEncryptor.decrypt(expense.getAmount());
		
		// create a copy of the expense because we dont want to decrypt the OG account in the DB
		Expense decryptedExpense = expense;
		decryptedExpense.setTitle(decryptedTitle);
		decryptedExpense.setAmount(decryptedAmount);

		return decryptedExpense;
	}
	
    // delete an expense
    public void deleteExpense(Long id) {
    	expenseRepo.deleteById(id);
    }
	
}
