package com.armando.myBudget.services;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.jasypt.util.numeric.AES256DecimalNumberEncryptor;
import org.jasypt.util.numeric.BasicIntegerNumberEncryptor;
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
	
	// decrypt cash account info
	public List<CashAcct> decryptCashAccts(List<CashAcct> cashaccounts) {
		System.out.println("Inside decryptCashAccts() in cash account service");
		// instanciate and pass password to the encryptors that were using
        AES256TextEncryptor aes256TextEncryptor = new AES256TextEncryptor();
        aes256TextEncryptor.setPassword(myKeys.getMelchor());
		
        List<CashAcct> decryptedCashAccounts = new ArrayList<CashAcct>();
        
		// iterate trough each Cash Account and decrypt the attributes in there
		for (int i=0; i < cashaccounts.size(); i++) {
			System.out.println("Inside for loop");
			CashAcct account = cashaccounts.get(i);
			System.out.println("Cash Account:");
			System.out.println(account.getTitle());
			
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
	
    // delete a cash account
    public void deleteCashAcct(Long id) {
    	cashAcctRepo.deleteById(id);
    }
	
}
