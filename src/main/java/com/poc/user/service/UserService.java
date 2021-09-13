package com.poc.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.poc.user.entity.Users;
import com.poc.user.repository.UserRepository;
import com.poc.user.request.UserRequest;
import com.poc.user.response.UserResponse;
import com.poc.user.utility.UserStatus;



@Service
public class UserService 
{
	@Autowired
	public UserRepository userRepo;

	//get all users in db
	public List<UserResponse> getAllUsers()
	{
		List<UserResponse> userList=new ArrayList<>();
		userRepo.findAll().forEach(u ->userList.add(convert(u)));
		return userList;
	}
	
	//get only active users
	public List<UserResponse> getActiveUsers()
	{
		List<UserResponse> activeUsers=new ArrayList<>();
		userRepo.findAllByStatus(UserStatus.ACTIVE.getStatusValue()).forEach(u -> activeUsers.add(convert(u)));
		return activeUsers;
	}
	
	/*search user by name
	public List<UserResponse> searchByfirstName(String name)
	{
		List<UserResponse> userList=new ArrayList<>();
		userRepo.findAllByfirstName(name).forEach(u -> userList.add(convert(u)));
		return userList;
	}
	*/
	
	public List<UserResponse> searchByAnyOne(String firstName, String lastName, String pincode)
	{
		List<UserResponse> userList=new ArrayList<>();
		userRepo.findAllByFirstNameOrLastNameOrPincode(firstName, lastName, pincode).forEach(u -> userList.add(convert(u)));
		return userList;
	}
	
	
	public Optional<UserResponse> updateUser(UserRequest userRqst, int userId)
	{
		Optional<Users> userRes = userRepo.findById(userId);
		Optional<UserResponse> returnObj=Optional.empty();
		if(userRes.isPresent())
		{
			Users user = userRes.get();
			return returnObj.of(convert(userRepo.save(UserService.update(userRqst, user))));
		}
		return null;	
	}
	
	public boolean deleteUser (int userId) {
		
		Optional<Users> user = userRepo.findById(userId);
		if(user.isPresent()) {
			userRepo.delete(user.get());
			return true;
		}
		else
			return false;
	}
	
	public boolean softDeleteUser(int userId) {
		
		Optional<Users> user = userRepo.findById(userId);
		if(user.isPresent()) {
			user.get().setStatus(UserStatus.INACTIVE.getStatusValue());
			userRepo.save(user.get());
			return true;
		}
		else
			return false;
	}
	
	public List<UserResponse> getAllUsersSortedBy(String byProperty)
	{
		List<UserResponse> userList=new ArrayList<>();
		
		userRepo.findAll(Sort.by(Direction.ASC,byProperty)).forEach(u ->userList.add(convert(u)));
		return userList;
	}
	
	
	public static Users update(UserRequest uObj, Users upUser)
	{
		upUser.setFirstName(uObj.getFirstName());
		upUser.setLastName(uObj.getLastName());
		upUser.setAddress(uObj.getAddress());
		upUser.setPincode(uObj.getPincode());
		upUser.setBirthDate(uObj.getBirthDate());
		upUser.setJoiningDate(uObj.getJoiningDate());
	
		return upUser;
		
	}
	
	
	
	public Optional<UserResponse> addUser(UserRequest userRqt)
	{
		//Optional<UserResponse> userResponse = Optional.empty();
		
		Users resObj = userRepo.save(UserService.convert(userRqt));
		
		System.out.println(resObj.getFirstName());
		
		return Optional.of(UserService.convert(resObj));
	}
	
	private static Users convert(UserRequest userRqst)
	{
		Users user=new Users();
		user.setFirstName(userRqst.getFirstName());
		user.setLastName(userRqst.getLastName());
		user.setAddress(userRqst.getAddress());
		user.setPincode(userRqst.getPincode());
		user.setBirthDate(userRqst.getBirthDate());
		user.setJoiningDate(userRqst.getJoiningDate());
		user.setStatus(UserStatus.ACTIVE.getStatusValue());
		return user;
	}
	
	private static UserResponse convert(Users userEntity)
	{
		
			if(userEntity!=null) {
					UserResponse userResponse=new UserResponse();
					
					userResponse.setUserId(userEntity.getUserId());
					userResponse.setFirstName(userEntity.getFirstName());
					userResponse.setLastName(userEntity.getLastName());
					userResponse.setAddress(userEntity.getAddress());
					userResponse.setPincode(userEntity.getPincode());
					userResponse.setBirthDate(userEntity.getBirthDate());
					userResponse.setJoiningDate(userEntity.getJoiningDate());
					userResponse.setStatus(UserStatus.GetUserStatusByValue(userEntity.getStatus()).name());
					
					return userResponse;
			}
			else
				return null;
	}
	
}
