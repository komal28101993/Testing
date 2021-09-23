package com.poc.student.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.student.model.Student;
import com.poc.student.repository.StudentRepository;
import com.poc.student.request.StudentRequest;
import com.poc.student.response.StudentResponse;
import com.poc.student.utils.StudentRequestMapper;

@Service
public class StudentService {

	@Autowired
	StudentRepository stdRepo;
	
	@Autowired
	StudentRequestMapper stdMapper;
	
	
	public StudentResponse addStudent(StudentRequest request) {
		
		return stdMapper.convertToResponse(stdRepo.save(stdMapper.convertToEntity(request)));
		
	}
	
	public List<StudentResponse> getAllStudents(){
		List<StudentResponse> result = new ArrayList<StudentResponse>();
	
		stdRepo.findAll().forEach(obj -> result.add(stdMapper.convertToResponse(obj)));
		return result;
		
	}
	
	public StudentResponse getStudentById(int id) {
		
		Optional<Student> result = stdRepo.findById(id);
		
		if(result.isPresent())
			
			return stdMapper.convertToResponse(result.get());
		else
			return null;
	}
	
	
}
