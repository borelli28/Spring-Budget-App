package com.armando.myBudget.services;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.jasypt.util.numeric.BasicIntegerNumberEncryptor;
import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.stereotype.Service;

import com.armando.myBudget.llaves.MyKeys;
import com.armando.myBudget.models.DueDate;
import com.armando.myBudget.models.Expense;
import com.armando.myBudget.repositories.DueDateRepo;

@Service
public class DueDateService {

	private final DueDateRepo duedateRepo;
    private final MyKeys myKeys;
    
    public DueDateService(DueDateRepo duedateRepo, MyKeys myKeys) {
    	this.duedateRepo = duedateRepo;
    	this.myKeys = myKeys;
    }
	
	public void createDuedate(DueDate duedate) {
		System.out.println("Inside createDuedate() in service");

        AES256TextEncryptor aes256TextEncryptor = new AES256TextEncryptor();
        aes256TextEncryptor.setPassword(myKeys.getMelchor());
		
        String datePlainText = duedate.getDate();       
        
    	String encryptedDate = aes256TextEncryptor.encrypt(datePlainText);
    	
    	System.out.println("Encrypted date value");
    	System.out.println(encryptedDate);
    	
    	duedate.setDate(encryptedDate);
    	duedateRepo.save(duedate);
	}
	
	public List<String> validateDueDate(DueDate duedate) {
		
		// initalize errors list
		List<String> DueDateErrors = new ArrayList<String>();
		
		String dateStr = duedate.getDate();
		
    	// check that date is not null or an empty string
    	if (dateStr.equals(null) || dateStr.equals("null") || dateStr.isBlank() || dateStr.length() == 0 || dateStr.length() > 2) {
    		DueDateErrors.add("Please enter a valid date");
    	  //  we need to check that dateStr is not null or empty before using parseInt() else it can throw an: java.lang.NumberFormatException
    	} else {
    		// convert date into int for validations
    		int date = Integer.parseInt(dateStr);
    		
    		// Check that date is not bigger than 31
        	if (date > 31) {
        		DueDateErrors.add("Please enter a number from 1 - 31");
        	}
        	
        	// Check that date is not less than 1
        	if (date < 1) {
        		DueDateErrors.add("Please enter a number from 1 - 31");
    	    }
    	}
    	
    	return DueDateErrors;
	}
	
	// decrypt List of due dates
	public List<DueDate> decryptDuedates(List<DueDate> duedates) {
		System.out.println("Inside decrypDuedates in expenses service");
		// instanciate and pass password to the encryptors that were using
        AES256TextEncryptor aes256TextEncryptor = new AES256TextEncryptor();
        aes256TextEncryptor.setPassword(myKeys.getMelchor());
		
        List<DueDate> decryptedDuedates = new ArrayList<DueDate>();

		// iterate trough each duedate and decrypt the date
		for (int i=0; i < duedates.size(); i++) {
			DueDate duedate = duedates.get(i);

			String decryptedDate = aes256TextEncryptor.decrypt(duedate.getDate());
			
			// create a copy of the duedate because we dont want to decrypt the original object in the DB
			DueDate decryptedObject = duedate;
			decryptedObject.setDate(decryptedDate);
			
			// add the decrypted duedate to the list of the decrypted duedates
			decryptedDuedates.add(decryptedObject);
		}
		return decryptedDuedates;
	}
	
	// decrypt a single due date
	public DueDate decryptDueDate(DueDate duedate) {
		// instanciate and pass password to the encryptors that were using
        AES256TextEncryptor aes256TextEncryptor = new AES256TextEncryptor();
        aes256TextEncryptor.setPassword(myKeys.getMelchor());
			
		String decryptedDate = aes256TextEncryptor.decrypt(duedate.getDate());
		
		// create a copy of the duedate because we dont want to decrypt the original object in the DB
		DueDate decryptedObject = duedate;
		decryptedObject.setDate(decryptedDate);

		return decryptedObject;
	}
	
    public void deleteDuedate(Long id) {
    	duedateRepo.deleteById(id);
    }
    
}
