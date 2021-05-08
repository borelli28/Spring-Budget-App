package com.armando.myBudget.repositories;

import com.armando.myBudget.models.DueDate;

public interface DueDateRepo {

	void deleteById(Long id);
	
	DueDate save(DueDate dueDate);
	
}
