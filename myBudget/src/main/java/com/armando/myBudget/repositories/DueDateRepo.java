package com.armando.myBudget.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.armando.myBudget.models.DueDate;

@Repository
public interface DueDateRepo extends CrudRepository<DueDate, Long>{

	void deleteById(Long id);
	
	DueDate save(DueDate dueDate);
	
}
