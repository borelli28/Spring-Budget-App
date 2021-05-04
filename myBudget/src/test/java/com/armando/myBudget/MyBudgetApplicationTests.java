package com.armando.myBudget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.armando.myBudget.controllers.UserController;
import com.armando.myBudget.models.User;

@SpringBootTest // test class
class MyBudgetApplicationTests {

	@Autowired
	private UserController userController;
	
	private static Validator validator;
	
	@BeforeAll
	static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	
	@Test // test case
	void contextLoads() {
	}
	
	@Test
	void testUserController() {
		assertThat(userController).isNotNull();
	}
	
	// Creates an empty instance of the User model and pass it to the validate method of the validator. 
	// Any violations of the validations we have set on the models,
	// will be returned as a Set.
	// If there is any violations, we can iterate through them and print them to the console.
	@Test
	void testUserModel() {
		User user = new User();
		
		user.setFirstName("Joe");
		user.setLastName("Mama");
		user.setEmail("some@email.com");
		user.setPassword("somepassword");
		user.setPasswordConfirmation("somepassword");
		
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		for (ConstraintViolation<User> violation : violations) {
			System.out.println(violation.getMessage());
		}
		assertTrue(violations.isEmpty());
	}

}
