package com.armando.myBudget.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min=2, message="First Name should be 2 or more characters long")
    private String firstName;
    
    @Size(min=2, message="Last Name should be 2 or more characters long")
    private String lastName;
    
    @Email(message="Email must be valid")
    @Size(min=3, message="Please enter an email")
    private String email;

    @Size(min=8, message="Password must be greater than 7 characters")
    private String password;
    
    @Transient
    private String passwordConfirmation;
    
    @Column(updatable=false)
    private Date createdAt;
    private Date updatedAt;
    
    // each user can have many cash accounts(checking acconut)
    @OneToMany(mappedBy="user", fetch=FetchType.LAZY)
    private List<CashAcct> cashAccts;
    
    // each user can have many Incomes
    @OneToMany(mappedBy="user", fetch=FetchType.LAZY)
    private List<Income> userIncomes;
    
    // each user will have many expenses
    @OneToMany(mappedBy="user", fetch=FetchType.LAZY)
    private List<Expense> expenses;
    
    // Assign a role to a User
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_roles", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;
    
	public User() {
	}
	
	public User(String firstName, String lastName, String email, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}


	// getters and setters
	@PrePersist
    protected void onCreate(){
        this.createdAt = new Date();
    }
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<CashAcct> getCashAccts() {
		return cashAccts;
	}

	public void setCashAccts(List<CashAcct> cashAccts) {
		this.cashAccts = cashAccts;
	}

	public List<Income> getIncomes() {
		return userIncomes;
	}

	public void setIncomes(List<Income> userIncomes) {
		this.userIncomes = userIncomes;
	}

	public List<Expense> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<Expense> expenses) {
		this.expenses = expenses;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	

	@PreUpdate
    protected void onUpdate(){
        this.updatedAt = new Date();
    }
}