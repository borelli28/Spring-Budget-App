package com.armando.myBudget.repositories;

import com.armando.myBudget.models.Income;

public interface IncomeRepo {

	void deleteById(Long id);
	
	Income save(Income income);
	
}
