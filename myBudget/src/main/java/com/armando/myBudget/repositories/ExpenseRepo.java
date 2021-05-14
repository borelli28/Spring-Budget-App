package com.armando.myBudget.repositories;

import org.springframework.stereotype.Repository;

import com.armando.myBudget.models.Expense;

@Repository
public interface ExpenseRepo {

	void deleteById(Long id);
	
	Expense save(Expense expense);
	
}
