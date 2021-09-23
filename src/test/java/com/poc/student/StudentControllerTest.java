package com.poc.student;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.student.controller.StudentController;
import com.poc.student.model.Project;
import com.poc.student.request.ProjectRequest;
import com.poc.student.request.StudentRequest;
import com.poc.student.response.StudentResponse;
import com.poc.student.service.StudentService;
import com.poc.student.utils.JwtUtil;

@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class StudentControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper mapper;

	@MockBean
	StudentService stdService;
	
	@MockBean
	JwtUtil jwtUtil;

	
	String token= null;

	List<Project> listResponseProjects = new ArrayList<Project>(Arrays.asList(new Project(1,"Tracking System","4 Months",null),new Project(2,"Binder Value Project","7 Weeks",null)));
	
	StudentResponse stdResponse = new StudentResponse(1, "Annie", "Trevin", 9652348562L, "annie.travin234@gmail.com", listResponseProjects);

	
	@Test
	@WithMockUser(username = "admin", password = "password123",authorities = "ROLE_ADMIN")
	public void getAllStudents_Success() throws Exception {
	
		List<StudentResponse> listOfStudents = new ArrayList<StudentResponse>(Arrays.asList(stdResponse));
		Mockito.when(stdService.getAllStudents()).thenReturn(listOfStudents);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/v1/student").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].lastName", is("Trevin")));
		
		
	}
	
	@Test
	@WithMockUser(username = "admin", password = "password123",authorities = "ROLE_ADMIN")
	public void getAllStudents_NoContent() throws Exception {
	
		List<StudentResponse> listOfStudents = new ArrayList<StudentResponse>(Arrays.asList());
		Mockito.when(stdService.getAllStudents()).thenReturn(listOfStudents);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/v1/student").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	
	}

	@Test
	@WithMockUser(username = "user", password = "user123",authorities = "ROLE_USER")
	public void addStudent_UserRole() throws Exception{
				
		List<ProjectRequest> listReqProjects = new ArrayList<>(Arrays.asList(ProjectRequest.builder().projectName("Tracking System").duration("4 Months").build(),ProjectRequest.builder().projectName("Binder Value Project").duration("7 Weeks").build()));
		StudentRequest stdRequest = StudentRequest.builder().fName("Annie").lName("Trevin").emailId("annie.travin234@gmail.com").mobileNo(9652348562L).prjDetails(listReqProjects).build();
		
		
		Mockito.when(stdService.addStudent(stdRequest)).thenReturn(stdResponse);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/student").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(this.mapper.writeValueAsString(stdRequest)).characterEncoding("utf-8");
		
		mockMvc.perform(mockRequest).andExpect(status().isBadRequest());
			
	}
	
	
	@Test
	@WithMockUser(username = "admin", password = "password123",authorities = "ROLE_ADMIN")
	public void addStudent_AdminRole() throws Exception{
		
		List<ProjectRequest> listReqProjects = new ArrayList<>(Arrays.asList(ProjectRequest.builder().projectName("Tracking System").duration("4 Months").build(),ProjectRequest.builder().projectName("Binder Value Project").duration("7 Weeks").build()));
		StudentRequest stdRequest = StudentRequest.builder().fName("Annie").lName("Trevin").emailId("annie.travin234@gmail.com").mobileNo(9652348562L).prjDetails(listReqProjects).build();
		
		
		Mockito.when(stdService.addStudent(stdRequest)).thenReturn(Mockito.mock(StudentResponse.class));
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/student").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(this.mapper.writeValueAsString(stdRequest)).characterEncoding("utf-8");
		
		mockMvc.perform(mockRequest).andExpect(status().isBadRequest());
			
	}
	
	@Test
	@WithMockUser(username = "admin", password = "password123",authorities = "ROLE_ADMIN")
	public void getStudentById_NotFound() throws Exception {
	
		Mockito.when(stdService.getStudentById(12)).thenReturn(null);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/v1/student/12").contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(mockRequest)
				.andExpect(status().isNotFound());
	
	}
	
	
	@Test
	@WithMockUser(username = "admin", password = "password123",authorities = "ROLE_ADMIN")
	public void getStudentById_Success() throws Exception {
	
		StudentResponse student = stdResponse;
		Mockito.when(stdService.getStudentById(1)).thenReturn(student);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/v1/student/1").contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(mockRequest).andExpect(status().isOk())
		.andExpect(jsonPath("$.lastName", is("Trevin")));
	}
	
	@Test
	@WithMockUser(username = "user", password = "user123",authorities = "ROLE_USER")
	public void getStudentById_Forbidden() throws Exception {
	
		StudentResponse student = stdResponse;
		Mockito.when(stdService.getStudentById(1)).thenReturn(student);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/v1/student/1").contentType(MediaType.APPLICATION_JSON);
		
		try {
			mockMvc.perform(mockRequest).andExpect(status().isForbidden());
			}
			catch(Exception ex) {
				Assert.isTrue(ex.getCause() instanceof AccessDeniedException);
				
			}
	}
	
	@Test
	@WithMockUser(username = "user", password = "user123",authorities = "ROLE_USER")
	public void getAllStudent_Forbidden() throws Exception {
	
		StudentResponse student = stdResponse;
		Mockito.when(stdService.getStudentById(1)).thenReturn(student);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/v1/student").contentType(MediaType.APPLICATION_JSON);
		try {
		mockMvc.perform(mockRequest).andExpect(status().isForbidden());
		}
		catch(Exception ex) {
			Assert.isTrue(ex.getCause() instanceof AccessDeniedException);
			
			
		}
	}
	


}
