package com.armando.myBudget.services;

import java.util.ArrayList;
import java.util.List;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.stereotype.Service;

import com.armando.myBudget.llaves.MyKeys;
import com.armando.myBudget.models.Income;
import com.armando.myBudget.repositories.IncomeRepo;

@Service
public class IncomeService {

	private final IncomeRepo incomeRepo;
    private final MyKeys myKeys;
	
	public IncomeService(IncomeRepo incomeRepo, MyKeys myKeys) {
		this.incomeRepo = incomeRepo;
		this.myKeys = myKeys;
	}
	
	public void createSaveIncome(Income income) {
		System.out.println("Inside createSaveIncome() in service");
		// encrypt title and amount of cash account before saving to the DB
        AES256TextEncryptor aes256TextEncryptor = new AES256TextEncryptor();
        aes256TextEncryptor.setPassword(myKeys.getMelchor());
		
        String titlePlainText = income.getTitle();        
        String amountPlainText = income.getAmount();
        
    	String encryptedTitle = aes256TextEncryptor.encrypt(titlePlainText);
    	String encryptedAmount = aes256TextEncryptor.encrypt(amountPlainText);
    	
    	income.setTitle(encryptedTitle);
    	income.setAmount(encryptedAmount);
        
		incomeRepo.save(income);
	}
	
    // validate income 
    public List<String> validateIncome(Income income) {
    	
		String title = income.getTitle();
		String amount = income.getAmount();
    	
		// initalize errors list
		List<String> incomeErrors = new ArrayList();
		
		// Check that inputs are valid( Title - a string with at least 2 character not null)
    	if (title.length() > 1 == false || title == null) {
    		incomeErrors.add("Please enter a title");
    	}
    	
    	// Check that amount is not null
    	if (amount == null) {
    		incomeErrors.add("Please enter an amount");
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
    			incomeErrors.add("Please enter two decimal digits after the number. Example: $250.00");
    		}	
	    }
    	
    	return incomeErrors;
    }
	
}
