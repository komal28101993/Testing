package com.poc.student.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.poc.student.filters.JwtRequestFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	JwtRequestFilter jwtRequestFilter;
	
	
	@Override
	public void configure(AuthenticationManagerBuilder authMgr) throws Exception {
		
		/*authMgr.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).
		withUser("user").password("$2a$10$MLm//SlquLPXlGPKBnZuAe57fay2uTMIkhumGX6XWLZGoUc45X7bS").authorities("ROLE_USER").and()
				.withUser("admin").password("$2a$10$hOsPW4H9Bc0hRUyAnYue8eGmlvlrfKG66LFvtPNeGx26TTpvH6DQy").authorities("ROLE_ADMIN");*/
		
		authMgr.userDetailsService(inMemoryUserDetailsManagerBean());
			
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		
		/*http.csrf().disable();
		http.cors();
		//http.authorizeRequests().anyRequest().fullyAuthenticated();
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/v1/student")
				.access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')").antMatchers(HttpMethod.GET, "/v1/student/*")
				.access("hasRole('ROLE_ADMIN')").antMatchers(HttpMethod.GET, "/v1/student")
				.access("hasRole('ROLE_ADMIN')");
		
		http.httpBasic();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);*/
		
		http.csrf().disable().authorizeRequests().antMatchers(HttpMethod.POST, "/v1/authenticate").permitAll()
				.anyRequest().authenticated().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

	}
	
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception{
		
		return super.authenticationManagerBean();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public InMemoryUserDetailsManager inMemoryUserDetailsManagerBean() {
		
		List<UserDetails> userList = new ArrayList<>();
		userList.add(User.withUsername("user").password(passwordEncoder().encode("user123")).authorities("ROLE_USER").build());
		userList.add(User.withUsername("admin").password(passwordEncoder().encode("password123")).authorities("ROLE_ADMIN").build());
		
		return new InMemoryUserDetailsManager(userList); 
	}
	
}
