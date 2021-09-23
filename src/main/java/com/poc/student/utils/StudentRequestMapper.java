package com.poc.student.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.poc.student.model.Project;
import com.poc.student.model.Student;
import com.poc.student.request.ProjectRequest;
import com.poc.student.request.StudentRequest;
import com.poc.student.response.StudentResponse;

@Component
public class StudentRequestMapper {
	
	@Autowired
	ProjectRequestMapper prjMapper;
	
	public Student convertToEntity(StudentRequest request) {
		
		Student entity = new Student();
		List<Project> projects = new ArrayList<Project>();
		
		entity.setFirstName(request.getFName());
		entity.setLastName(request.getLName());
		entity.setEmailAddress(request.getEmailId());
		entity.setMobileNumber(request.getMobileNo());
		request.getPrjDetails().stream().forEach(obj -> projects.add(prjMapper.convertToEntity(obj,entity)));

		entity.setListOfProjects(projects);
		
		return entity;
	}

	public StudentResponse convertToResponse(Student entity) {
		
		StudentResponse response = new StudentResponse();
		response.setFirstName(entity.getFirstName());
		response.setLastName(entity.getLastName());
		response.setMobileNumber(entity.getMobileNumber());
		response.setId(entity.getStudentId());
		response.setListOfProjects(entity.getListOfProjects());
		response.setEmailAddress(entity.getEmailAddress());
		
		return response;
		
	}
	

}
