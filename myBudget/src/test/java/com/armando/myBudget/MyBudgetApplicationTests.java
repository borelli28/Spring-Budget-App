package com.armando.myBudget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import com.armando.myBudget.models.DueDate;
import com.armando.myBudget.models.Expense;
import com.armando.myBudget.models.Role;
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
		assertTrue(violations.isEmpty(), "When passed valid User attributes it did trigger some validations errors");
		
		//checks that invalids inputs trigger all possible validations errors(4)
		user.setFirstName("");
		user.setLastName("");
		user.setEmail("s");
		user.setPassword("s");
		user.setPasswordConfirmation("0");
		
		Set<ConstraintViolation<User>> violationsTwo = validator.validate(user);
		assertFalse(violationsTwo.isEmpty(), "User model validations error were not triggered by our invalid inputs"); 
	}
	
	@Test
	void testRoleModel() {
		Role role = new Role();
		
		// checks that a valid name creates a new Role
		role.setName("GUEST");
		Set<ConstraintViolation<Role>> violations = validator.validate(role);
		for (ConstraintViolation<Role> violation : violations) {
			System.out.println(violation.getMessage());
		}
		assertTrue(violations.isEmpty(), "When passed a valid Role name it did trigger some validations errors");
		
		// check that an empty name triggers the validation error
		role.setName("");
		Set<ConstraintViolation<Role>> violationsTwo = validator.validate(role);
		assertFalse(violationsTwo.isEmpty(), "Role name validation did not trigger when passed an empty String");
	}
	
	@Test
	void testExpenseModel() {
		Expense expense = new Expense();
		
		// test for valid attributes
		expense.setTitle("My expense title");
		expense.setAmount(12.00);
		Set<ConstraintViolation<Expense>> violations = validator.validate(expense);
		for (ConstraintViolation<Expense> violation : violations) {
			System.out.println(violation.getMessage());
		}
		assertTrue(violations.isEmpty(), "When passed a valid Expense attributes it did trigger some validations errors");
		
		// check for invalid attributes to trigger the validation errors
		expense.setTitle("");
		expense.setAmount(null);
		Set<ConstraintViolation<Expense>> violationsTwo = validator.validate(expense);
		assertFalse( violationsTwo.isEmpty(), "Invalid Expense attributes did not trigger validations errors");
	}
	
	@Test
	void testDueDateModel() {
		DueDate dueDate = new DueDate();
		
		// check for a valid date
		dueDate.setDate(11);
		Set<ConstraintViolation<DueDate>> violations = validator.validate(dueDate);
		for (ConstraintViolation<DueDate> violation : violations) {
			System.out.println(violation.getMessage());
		}
		assertTrue(violations.isEmpty(), "When passed a valid Due Date it returns an validation error");
		
		// check for invalid date
		dueDate.setDate(45);
		Set<ConstraintViolation<DueDate>> violationsTwo = validator.validate(dueDate);
		assertFalse( violationsTwo.isEmpty(), "When passed an invalid date it did not trigger a validation errors");
	}

}
