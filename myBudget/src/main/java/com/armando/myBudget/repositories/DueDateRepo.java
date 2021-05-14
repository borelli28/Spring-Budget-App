package com.armando.myBudget.repositories;

import org.springframework.stereotype.Repository;

import com.armando.myBudget.models.DueDate;

@Repository
public interface DueDateRepo {

	void deleteById(Long id);
	
	DueDate save(DueDate dueDate);
	
}
