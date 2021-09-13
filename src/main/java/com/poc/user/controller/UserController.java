package com.poc.user.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poc.user.entity.Users;
import com.poc.user.request.UserRequest;
import com.poc.user.response.UserResponse;
import com.poc.user.service.UserService;

@RestController
@RequestMapping("/v1/users")
public class UserController 
{
	@Autowired
	public UserService userService;
	
	@PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserResponse> addUser(@Valid @RequestBody UserRequest uReq)
	{
		Optional<UserResponse> response = userService.addUser(uReq);
		
		if(response.isPresent()) {

			return new ResponseEntity<UserResponse>(response.get(), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
	}
	
	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserResponse>> getAllUsers()
	{
		List<UserResponse> result = userService.getAllUsers();
		
		if(!result.isEmpty())
		return new ResponseEntity<>(result,HttpStatus.OK);
		else
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	
	}
	
	@GetMapping(value = "/active", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserResponse>> getAllActiveUsers()
	{
		return new ResponseEntity<>(userService.getActiveUsers(),HttpStatus.OK);
	
	}
	
	@GetMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserResponse>> getAllUsersByName(
			@RequestParam(name = "firstName", required = false) String name,
			@RequestParam(name = "lastName", required = false) String lstName,
			@RequestParam(name = "pincode", required = false) String pin)
	{
		List<UserResponse> result = userService.searchByAnyOne(name, lstName, pin);
		
		if(!result.isEmpty())
			return new ResponseEntity<>(result,HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);	
	}
	
	
	@PutMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserResponse> updateUser(@PathVariable int userId, @Valid @RequestBody UserRequest userObj)
	{
		Optional<UserResponse> response =	userService.updateUser(userObj, userId);
		 if(response.isPresent())
			 return new ResponseEntity<UserResponse>(response.get(), HttpStatus.OK);
		 else
			 return new ResponseEntity<>(HttpStatus.NO_CONTENT);

	}
	
	@DeleteMapping(value="/{userId}")
	public ResponseEntity<HttpStatus> deleteUser(@PathVariable int userId){
		
		if(userService.deleteUser(userId))
		{
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PatchMapping(value = "/{userId}")
	public ResponseEntity<HttpStatus> softDeleteUser(@PathVariable int userId)
	{
		
		if(userService.softDeleteUser(userId)) 
			return new ResponseEntity<>(HttpStatus.OK);
		
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			
	}
	
	@GetMapping(value ="/sort", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserResponse>> getAllUsersSortedByDOB(@RequestParam(name = "by", required = false) String colName)
	{
		return new ResponseEntity<>(userService.getAllUsersSortedBy(colName),HttpStatus.OK);
	
	}

}
