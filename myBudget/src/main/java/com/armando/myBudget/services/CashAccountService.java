package com.armando.myBudget.services;

import java.math.BigDecimal;

import org.jasypt.util.numeric.AES256DecimalNumberEncryptor;
import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.stereotype.Service;

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
        // need a different algorithm to encrypt numbers
        AES256DecimalNumberEncryptor aes256DecimalEncryptor = new AES256DecimalNumberEncryptor();
        aes256DecimalEncryptor.setPassword(myKeys.getMelchor());
		
        String titlePlainText = cashaccount.getTitle();
        BigDecimal amountPlainText = cashaccount.getAmount();
        
    	String encryptedTitle = aes256TextEncryptor.encrypt(titlePlainText);
    	BigDecimal encryptedAmount = aes256DecimalEncryptor.encrypt(amountPlainText);

    	System.out.println("Encryted attributes:");
    	System.out.println(encryptedTitle);
    	System.out.println(encryptedAmount);
    	
    	cashaccount.setTitle(encryptedTitle);
    	cashaccount.setAmount(encryptedAmount);
        
		cashAcctRepo.save(cashaccount);
	}
	
    // delete a cash account
    public void deleteCashAcct(Long id) {
    	cashAcctRepo.deleteById(id);
    }
	
}
