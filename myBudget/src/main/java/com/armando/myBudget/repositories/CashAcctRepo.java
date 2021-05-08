package com.armando.myBudget.repositories;

import com.armando.myBudget.models.CashAcct;

public interface CashAcctRepo {

	void deleteById(Long id);
	
	CashAcct save(CashAcct cashAcct);
	
}
