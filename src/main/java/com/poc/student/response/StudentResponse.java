package com.poc.student.response;

import java.util.List;

import com.poc.student.model.Project;

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
public class StudentResponse {

	
	private int Id;
	private String firstName;
	private String lastName;
	private long mobileNumber;
	private String emailAddress;
	
	private List<Project> listOfProjects;
	
}
