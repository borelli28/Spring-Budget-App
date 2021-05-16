package com.armando.myBudget.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.armando.myBudget.models.Expense;

@Repository
public interface ExpenseRepo extends CrudRepository<Expense, Long> {

	void deleteById(Long id);
	
	Expense save(Expense expense);
	
}
