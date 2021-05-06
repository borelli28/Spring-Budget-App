package com.armando.myBudget;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
 
    // add Bcyrpt bean
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http.
	        authorizeRequests() // Allows restricting access based upon the HttpServletRequest
	        .antMatchers("/css/**", "/js/**", "/registration").permitAll() // PathMatcher implementation for Ant-style path patterns.
	        .antMatchers("/admin/**").access("hasRole('ADMIN')") // Any URL that starts with "/admin" requires the user to have ROLE_ADMIN
	        .anyRequest().authenticated() // Maps any request // authenticated(): Specify URLs that are allowed by authenticated users only
	        .and()
	    .formLogin() //  Specifies to support form based authentication. Now, our users are going to be authenticated via a FORM
	        .loginPage("/login") // Specifies the URL to send users to if login is required
	        .permitAll()
	        .and()
	    .logout()
	        .permitAll();
	// logout(): Provides logout support. 
	// The default is that accessing the URL "/logout" will log the user out by invalidating the HTTP Session,
	// cleaning up any rememberMe() authentication that was configured, 
	// clearing the SecurityContextHolder, and then redirect to "/login?success".
	}
}
