package com.armando.myBudget.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import com.armando.myBudget.llaves.MyKeys;
import com.armando.myBudget.models.CashAcct;
import com.armando.myBudget.repositories.CashAcctRepo;

@Service
public class CashAccountService {

	private final CashAcctRepo cashAcctRepo;
    private final MyKeys myKeys;
	
	public CashAccountService(CashAcctRepo cashAcctRepo, MyKeys myKeys) {
		this.cashAcctRepo = cashAcctRepo;
		this.myKeys = myKeys;
	}
	
	public void createSaveAcct(CashAcct cashaccount) {
		System.out.println("Inside createSaveAcct()");
		// encrypt title and amount of cash account before saving to the DB
        AES256TextEncryptor aes256TextEncryptor = new AES256TextEncryptor();
        aes256TextEncryptor.setPassword(myKeys.getMelchor());
		
        String titlePlainText = cashaccount.getTitle();        
        String amountPlainText = cashaccount.getAmount();
        
    	String encryptedTitle = aes256TextEncryptor.encrypt(titlePlainText);
    	String encryptedAmount = aes256TextEncryptor.encrypt(amountPlainText);
    	
    	System.out.println("Encryted attributes:");
    	System.out.println(encryptedTitle);
    	System.out.println(encryptedAmount);
    	
    	cashaccount.setTitle(encryptedTitle);
    	cashaccount.setAmount(encryptedAmount);
        
		cashAcctRepo.save(cashaccount);
	}
	
	public void updateCashAcct(CashAcct cashaccount, Long cashAcctId) {
		
		System.out.println("Inside updateSaveAcct()");
		// get OG cash account instance that we are editing
		Optional<CashAcct> ogAccount = cashAcctRepo.findById(cashAcctId);
		CashAcct editThisAcct = new CashAcct();
		if (ogAccount.isPresent()) {
			editThisAcct = ogAccount.get();
		}
		
		// encrypt title and amount of cash account before saving to the DB
        AES256TextEncryptor aes256TextEncryptor = new AES256TextEncryptor();
        aes256TextEncryptor.setPassword(myKeys.getMelchor());
		
        String titlePlainText = cashaccount.getTitle();        
        String amountPlainText = cashaccount.getAmount();
        
    	String encryptedTitle = aes256TextEncryptor.encrypt(titlePlainText);
    	String encryptedAmount = aes256TextEncryptor.encrypt(amountPlainText);
    	
    	editThisAcct.setTitle(encryptedTitle);
    	editThisAcct.setAmount(encryptedAmount);
        
		cashAcctRepo.save(editThisAcct);
	}
	
	// decrypt List of cash accounts
	public List<CashAcct> decryptCashAccts(List<CashAcct> cashaccounts) {
		System.out.println("Inside decryptCashAccts() in cash account service");
		// instanciate and pass password to the encryptors that were using
        AES256TextEncryptor aes256TextEncryptor = new AES256TextEncryptor();
        aes256TextEncryptor.setPassword(myKeys.getMelchor());
		
        List<CashAcct> decryptedCashAccounts = new ArrayList<CashAcct>();
        
		// iterate trough each Cash Account and decrypt the attributes in there
		for (int i=0; i < cashaccounts.size(); i++) {
			CashAcct account = cashaccounts.get(i);
			
			String decryptedTitle = aes256TextEncryptor.decrypt(account.getTitle());
			String decryptedAmount = aes256TextEncryptor.decrypt(account.getAmount());
			
			// create a copy of the account because we dont want to decrypt the OG account in the DB
			CashAcct decryptedAccount = account;
			decryptedAccount.setTitle(decryptedTitle);
			decryptedAccount.setAmount(decryptedAmount);
			
			// add the decrypted account to the list of the decrypted accounts
			decryptedCashAccounts.add(i, decryptedAccount);
		}
		System.out.println("Done decrypting all cash accounts info now return the list");
		return decryptedCashAccounts;
	}
	
	// decrypt a cash info
	public CashAcct decryptCashAcct(CashAcct cashaccount) {
		// instanciate and pass password to the encryptors that were using
        AES256TextEncryptor aes256TextEncryptor = new AES256TextEncryptor();
        aes256TextEncryptor.setPassword(myKeys.getMelchor());
			
		String decryptedTitle = aes256TextEncryptor.decrypt(cashaccount.getTitle());
		String decryptedAmount = aes256TextEncryptor.decrypt(cashaccount.getAmount());
		
		// create a copy of the account because we dont want to decrypt the OG account in the DB
		CashAcct decryptedAccount = cashaccount;
		decryptedAccount.setTitle(decryptedTitle);
		decryptedAccount.setAmount(decryptedAmount);

		return decryptedAccount;
	}
	
    // delete a cash account
    public void deleteCashAcct(Long id) {
    	cashAcctRepo.deleteById(id);
    }
    
    // validate cash account attributes
    public List<String> validateAccount(CashAcct cashaccount) {
    	
		String title = cashaccount.getTitle();
		String amount = cashaccount.getAmount();
    	
		// initalize errors list
		List<String> cashAccountErrors = new ArrayList();
		
		// Check that inputs are valid( Title - a string with at least 2 character not null)
    	if (title.length() > 1 == false || title == null) {
    		cashAccountErrors.add("Please enter a title");
    	}
    	
    	// Check that amount is not null
    	if (amount == null) {
    		cashAccountErrors.add("Please enter an amount");
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
    			cashAccountErrors.add("Please enter two decimal digits after the number. Example: $250.00");
    		}	
	    }
    	
    	return cashAccountErrors;
    }
	
}
