package com.armando.myBudget.repositories;

import org.springframework.stereotype.Repository;

import com.armando.myBudget.models.Income;

@Repository
public interface IncomeRepo {

	void deleteById(Long id);
	
	Income save(Income income);
	
}
