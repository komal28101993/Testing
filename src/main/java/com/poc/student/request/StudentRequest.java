package com.poc.student.request;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import com.sun.istack.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StudentRequest {

	@NotNull
	@Pattern(regexp = "[a-zA-z]+")
	private String fName;
	
	@Pattern(regexp = "[a-zA-z]+")
	private String lName;
	
	@NotNull
	private long mobileNo;
	
	@Email
	private String emailId;
	
	@NotNull
	private List<ProjectRequest> prjDetails;
	
}
