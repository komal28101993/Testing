package com.poc.user.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.poc.user.entity.Users;

@Repository
public interface UserRepository extends PagingAndSortingRepository<Users, Integer> {

	public List<Users> findAllByStatus(int status);
	public List<Users> findAllByFirstNameOrLastNameOrPincode(String firstName, String lastName, String pincode);
}
