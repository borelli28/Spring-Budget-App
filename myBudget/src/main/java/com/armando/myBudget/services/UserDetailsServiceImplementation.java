package com.armando.myBudget.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.armando.myBudget.models.Role;
import com.armando.myBudget.models.User;
import com.armando.myBudget.repositories.UserRepo;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {
    private UserRepo userRepo;
    
    public UserDetailsServiceImplementation(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    // On a login attempt(login post) spring will call this method.
    // from there spring has two options:
    // 1. Successful Login: The user is authenticated, saves them in a context, and redirects to "/"
    // 2. Unsuccessful Login: The client is redirected to "/login?error".
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);
        System.out.println("Inside loadByUsername method in UserDetailsServiceImplementation.java");
        System.out.println("Email:");
        System.out.println(email);
        if(user == null) {
        	System.out.println("user not found");
            throw new UsernameNotFoundException("User not found");
        }
        
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthorities(user));
    }
    
    // get user permissions
    private List<GrantedAuthority> getAuthorities(User user){
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for(Role role : user.getRoles()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getName());
            authorities.add(grantedAuthority);
        }
        return authorities;
    }
}
