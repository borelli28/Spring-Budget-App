package com.armando.myBudget.repositories;

import com.armando.myBudget.models.Expense;

public interface ExpenseRepo {

	void deleteById(Long id);
	
	Expense save(Expense expense);
	
}
