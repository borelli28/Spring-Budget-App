package com.armando.myBudget.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.armando.myBudget.models.Income;

@Repository
public interface IncomeRepo extends CrudRepository<Income, Long>{

	void deleteById(Long id);
	
	Income save(Income income);
	
}
