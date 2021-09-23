package com.poc.student.utils;

import org.springframework.stereotype.Component;

import com.poc.student.model.Project;
import com.poc.student.model.Student;
import com.poc.student.request.ProjectRequest;

@Component
public class ProjectRequestMapper {

	public Project convertToEntity(ProjectRequest request, Student stdObj) {
		Project entity = new Project();
		entity.setProjectName(request.getProjectName());
		entity.setDuration(request.getDuration());
		entity.setStudent(stdObj);
		
		return entity;
	}
}
