package com.poc.student.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poc.student.request.StudentRequest;
import com.poc.student.response.StudentResponse;
import com.poc.student.service.StudentService;

@RestController
@RequestMapping(value = "/v1/student")
public class StudentController {
	
	@Autowired
	StudentService stdService;
	
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	@PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces= MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> insertStudent(@Valid @RequestBody StudentRequest request){
		
		StudentResponse result = stdService.addStudent(request);
		if(result != null) {
			return new ResponseEntity<StudentResponse>(result, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<StudentResponse>> getAllStudents(){
		
		List<StudentResponse> results = stdService.getAllStudents();
		if(results.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		else {
			return new ResponseEntity<List<StudentResponse>>(results, HttpStatus.OK);
		}		
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/{Id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getStudentById(@PathVariable(name = "Id") int studentId){
		
		StudentResponse result = stdService.getStudentById(studentId);
		
		if(result != null)
			return new ResponseEntity<StudentResponse>(result, HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
