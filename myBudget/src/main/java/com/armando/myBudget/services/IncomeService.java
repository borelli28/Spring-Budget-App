package com.armando.myBudget.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.stereotype.Service;

import com.armando.myBudget.llaves.MyKeys;
import com.armando.myBudget.models.CashAcct;
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
    
//	public void updateCashAcct(CashAcct cashaccount, Long cashAcctId) {
//		
//		System.out.println("Inside updateSaveAcct()");
//		// get OG cash account instance that we are editing
//		Optional<CashAcct> ogAccount = cashAcctRepo.findById(cashAcctId);
//		CashAcct editThisAcct = new CashAcct();
//		if (ogAccount.isPresent()) {
//			editThisAcct = ogAccount.get();
//		}
//		
//		// encrypt title and amount of cash account before saving to the DB
//        AES256TextEncryptor aes256TextEncryptor = new AES256TextEncryptor();
//        aes256TextEncryptor.setPassword(myKeys.getMelchor());
//		
//        String titlePlainText = cashaccount.getTitle();        
//        String amountPlainText = cashaccount.getAmount();
//        
//    	String encryptedTitle = aes256TextEncryptor.encrypt(titlePlainText);
//    	String encryptedAmount = aes256TextEncryptor.encrypt(amountPlainText);
//    	
//    	editThisAcct.setTitle(encryptedTitle);
//    	editThisAcct.setAmount(encryptedAmount);
//        
//		cashAcctRepo.save(editThisAcct);
//	}
	
	// decrypt List of incomes
	public List<Income> decryptIncomes(List<Income> incomes) {
		System.out.println("Inside decryptCashAccts() in cash account service");
		// instanciate and pass password to the encryptors that were using
        AES256TextEncryptor aes256TextEncryptor = new AES256TextEncryptor();
        aes256TextEncryptor.setPassword(myKeys.getMelchor());
		
        List<Income> decryptedIncomes = new ArrayList<Income>();
        
		// iterate trough each income and decrypt the attributes in there
		for (int i=0; i < incomes.size(); i++) {
			Income income = incomes.get(i);
			
			String decryptedTitle = aes256TextEncryptor.decrypt(income.getTitle());
			String decryptedAmount = aes256TextEncryptor.decrypt(income.getAmount());
			
			// create a copy of the income because we dont want to decrypt the OG object in the DB
			Income decryptedIncome = income;
			decryptedIncome.setTitle(decryptedTitle);
			decryptedIncome.setAmount(decryptedAmount);
			
			// add the decrypted income to the list of the decrypted incomes
			decryptedIncomes.add(i, decryptedIncome);
		}
		return decryptedIncomes;
	}
	
	// decrypt a single income instance
	public Income decryptIncome(Income income) {
		// instanciate and pass password to the encryptors that were using
        AES256TextEncryptor aes256TextEncryptor = new AES256TextEncryptor();
        aes256TextEncryptor.setPassword(myKeys.getMelchor());
			
		String decryptedTitle = aes256TextEncryptor.decrypt(income.getTitle());
		String decryptedAmount = aes256TextEncryptor.decrypt(income.getAmount());
		
		// create a copy of the income because we dont want to decrypt the original object in the DB
		Income decryptedIncome = income;
		decryptedIncome.setTitle(decryptedTitle);
		decryptedIncome.setAmount(decryptedAmount);

		return decryptedIncome;
	}
	
    public void deleteIncome(Long id) {
    	incomeRepo.deleteById(id);
    }
	
}
