package com.armando.myBudget.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.armando.myBudget.models.Role;


@Repository
public interface RoleRepo extends CrudRepository<Role, Long>{

	List<Role> findAll();
	
	List<Role> findByName(String name);
	
}