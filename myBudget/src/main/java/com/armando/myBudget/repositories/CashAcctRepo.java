package com.armando.myBudget.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.armando.myBudget.models.CashAcct;

@Repository
public interface CashAcctRepo extends CrudRepository<CashAcct, Long> {

	void deleteById(Long id);
	
	CashAcct save(CashAcct cashAcct);
	
}
