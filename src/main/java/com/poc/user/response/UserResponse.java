package com.poc.user.response;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class UserResponse {
	
	private int userId;
	private String firstName;
	private String lastName;
	private String address;
	private String pincode;
	private LocalDate birthDate;
	private LocalDate joiningDate;
	private String status;

}
