package com.armando.myBudget.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.armando.myBudget.models.CashAcct;

@Repository
public interface CashAcctRepo extends CrudRepository<CashAcct, Long> {

	void deleteById(Long id);
	
	Optional<CashAcct> findById(Long c);
	
	CashAcct save(CashAcct cashAcct);
	
}
