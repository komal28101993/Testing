package com.poc.user.request;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
public class UserRequest {

	//private int userId;
	@NotNull
	@Pattern(regexp = "[A-Za-z]+")
	private String firstName;
	
	@NotNull
	@Pattern(regexp = "[A-Za-z]+")
	private String lastName;
	
	@NotNull
	private String address;
	
	@NotNull
	@Pattern(regexp = "[0-9]+")
	private String pincode;
	
	@NotNull
	private LocalDate birthDate;
	
	@NotNull
	private LocalDate joiningDate;
	//private int status;
}
