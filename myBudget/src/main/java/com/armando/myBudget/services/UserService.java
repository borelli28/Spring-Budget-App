package com.armando.myBudget.services;

import org.springframework.stereotype.Service;

@Service
public class UserService {
	
//    private UserRepo userRepo;
//    private RoleRepo roleRepo;
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//    
//    public UserService(UserRepo userRepo, RoleRepo roleRepo, BCryptPasswordEncoder bCryptPasswordEncoder)     {
//    	this.userRepo = userRepo;
//    	this.roleRepo = roleRepo;
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//    }
//    // Saves a client with only the user role.
//    public void saveWithUserRole(User user) {
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//        user.setRoles(roleRepo.findByName("ROLE_USER"));
//        userRepo.save(user);
//    }
//    // Saves a client with only the admin role.
//    public void saveUserWithAdminRole(User user) {
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//        user.setRoles(roleRepo.findByName("ROLE_ADMIN"));
//        userRepo.save(user);
//    }    
//    // Finds a user by their username.
//    public User findByUsername(String username) {
//        return userRepo.findByUsername(username);
//    }
}
