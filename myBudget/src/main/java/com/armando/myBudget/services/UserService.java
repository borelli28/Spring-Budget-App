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
