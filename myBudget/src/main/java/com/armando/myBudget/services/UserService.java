package com.armando.myBudget.services;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.armando.myBudget.repositories.UserRepo;
import com.armando.myBudget.repositories.RoleRepo;
import com.armando.myBudget.llaves.MyKeys;
import com.armando.myBudget.models.User;

@Service
public class UserService {
	
	private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MyKeys myKeys;
    
    public UserService(UserRepo userRepo, RoleRepo roleRepo, BCryptPasswordEncoder bCryptPasswordEncoder, MyKeys myKeys)     {
    	this.userRepo = userRepo;
    	this.roleRepo = roleRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.myKeys = myKeys;
    }
    
    // Saves a client with only the user role.
    public void saveWithUserRole(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(roleRepo.findByName("ROLE_USER"));
        // firstName & lastName
        String firstNamePlainText = user.getFirstName();
        String lastNamePlainText = user.getLastName();
        
        AES256TextEncryptor aes256TextEncryptor = new AES256TextEncryptor();
        aes256TextEncryptor.setPassword(myKeys.getMelchor());
        
        // encrypt first and last name
        String encryptedFirstName = aes256TextEncryptor.encrypt(firstNamePlainText);
    	String encryptedLastName = aes256TextEncryptor.encrypt(lastNamePlainText);

    	user.setFirstName(encryptedFirstName);
    	user.setLastName(encryptedLastName);
        
        userRepo.save(user);
    }
    
    // Saves a client with only the admin role.
    public void saveUserWithAdminRole(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(roleRepo.findByName("ROLE_ADMIN"));
        // before saving the user into the DB we need to encrypt:
        // firstName & lastName
        
        String firstNamePlainText = user.getFirstName();
        String lastNamePlainText = user.getLastName();
        
        AES256TextEncryptor aes256TextEncryptor = new AES256TextEncryptor();
        aes256TextEncryptor.setPassword(myKeys.getMelchor());
        
        // encrypt first and last name
        String encryptedFirstName = aes256TextEncryptor.encrypt(firstNamePlainText);
    	String encryptedLastName = aes256TextEncryptor.encrypt(lastNamePlainText);

    	user.setFirstName(encryptedFirstName);
    	user.setLastName(encryptedLastName);
        
        userRepo.save(user);
    }
    
    public void updateUserName(User newUser, User user) {
        AES256TextEncryptor aes256TextEncryptor = new AES256TextEncryptor();
        aes256TextEncryptor.setPassword(myKeys.getMelchor());
        
		// if user enter a first name then first name will be updated
		if (newUser.getFirstName() != null) {
			String firstNamePlainText = newUser.getFirstName();
			String encryptedFirstName = aes256TextEncryptor.encrypt(firstNamePlainText);
			user.setFirstName(encryptedFirstName);
		}
		// if user enter a last name then user last name will be updated
    	if (newUser.getLastName() != null) {
			String lastNamePlainText = newUser.getLastName();
			String encryptedLastName = aes256TextEncryptor.encrypt(lastNamePlainText);
			user.setLastName(encryptedLastName);
    	}
    	
    	userRepo.save(user);
    	System.out.println("User updated!");
    }
    
    public void updateUserEmail(String email, User user) {
    	 
		// if user enter an email then user email will be updated
    	if (email != null) {
			user.setEmail(email);
    	}
    	
    	userRepo.save(user);
    	System.out.println("User updated!");
    }
    
    public void updateUserPassword(String password, User user) {
   	 
		// if user enter a password then the password field will be updated
    	if (password != null) {
			user.setPassword(password);
			// hash the password with bcrypt
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    	}
    	
    	userRepo.save(user);
    	System.out.println("User updated!");
    }
    
    // Finds a user by their email
    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }
    
    // unencrypted user data and return the unencrypted user
    public User decryptUser(User user) {
    	User UserDecrypt = user;
    	// create Encryptor algo instance and enter the password
    	AES256TextEncryptor aes256TextEncryptor = new AES256TextEncryptor();
    	aes256TextEncryptor.setPassword(myKeys.getMelchor());
    	// decrypt user first name and then assign the plain text first name to the user
    	String userFirstName = aes256TextEncryptor.decrypt(user.getFirstName());
    	UserDecrypt.setFirstName(userFirstName);
    	// decrypt user last name and then assign the plain text last name to the user
    	String userLastName = aes256TextEncryptor.decrypt(user.getLastName());
    	UserDecrypt.setLastName(userLastName);

    	return UserDecrypt;
    }
}
