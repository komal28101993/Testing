package com.poc.student.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poc.student.request.AuthRequest;
import com.poc.student.response.AuthResponse;
import com.poc.student.utils.JwtUtil;

@RestController
@RequestMapping(value = "/v1")
public class AuthenticateController {

	@Autowired
	AuthenticationManager authManager;
		
	@Autowired
	InMemoryUserDetailsManager userDetailsManager;
	

	@Autowired
	JwtUtil jwtUtil;
	
	@PostMapping(value = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAuthToken(@RequestBody AuthRequest request) throws Exception{
		
		Authentication authentication = null;
		
		try {
			   authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		
		}
		catch(BadCredentialsException ex){
			throw new Exception("Incorrect Username or Password",ex);
		}
			
		final String token = jwtUtil.generateToken(authentication);
		
		return ResponseEntity.ok(new AuthResponse(token));
	}
}
