package com.armando.myBudget.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.armando.myBudget.models.User;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {
	
	User findByEmail(String email);

	List<User> findAll();

	Optional<User> findById(Long i);
	
	void deleteById(Long id);
	
	User save(User user);
}