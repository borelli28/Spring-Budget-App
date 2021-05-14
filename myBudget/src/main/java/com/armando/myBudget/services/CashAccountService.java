package com.armando.myBudget.services;

import org.springframework.stereotype.Service;

import com.armando.myBudget.models.CashAcct;
import com.armando.myBudget.repositories.CashAcctRepo;

@Service
public class CashAccountService {

	private final CashAcctRepo cashAcctRepo;
	
	public CashAccountService(CashAcctRepo cashAcctRepo) {
		this.cashAcctRepo = cashAcctRepo;
	}
	
	public void createSaveAcct(CashAcct cashaccount) {
		System.out.println("Inside createSaveAcct()");
		cashAcctRepo.save(cashaccount);
	}
	
}
