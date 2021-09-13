package com.poc.user.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.user.request.UserRequest;
import com.poc.user.response.UserResponse;
import com.poc.user.service.UserService;
import com.poc.user.utility.UserStatus;

@WebMvcTest(UserController.class)
public class UserControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper mapper;

	@MockBean
	UserService uService;

	UserResponse userRes1 = new UserResponse(1,"Jacob","Kennel","#567, Sector-16B, Chandigarh","160016",LocalDate.of(1992,Month.AUGUST, 12),LocalDate.of(2021,Month.JUNE, 7),UserStatus.ACTIVE.name());
	UserResponse userRes2 = new UserResponse(2,"Anna","Harris","#487, Sector-1D, Chandigarh","160001",LocalDate.of(1987,Month.FEBRUARY, 22),LocalDate.of(2005,Month.FEBRUARY, 17),UserStatus.ACTIVE.name());
	UserResponse userRes3 = new UserResponse(3,"Greg","Jeddy","#59, Sector-27A, Chandigarh","160027",LocalDate.of(1978,Month.MARCH, 2),LocalDate.of(1999,Month.JULY, 4),UserStatus.ACTIVE.name());
	UserResponse userRes4 = new UserResponse(4,"Dennis","Stook","#6113, Sector-12B, Chandigarh","160012",LocalDate.of(1989,Month.MAY, 3),LocalDate.of(2020,Month.JANUARY, 3),UserStatus.ACTIVE.name());
	
	
	@Test
	public void getAllUsers_Success() throws Exception {
	
		List<UserResponse> listOfUsers = new ArrayList<UserResponse>(Arrays.asList(userRes1,userRes2,userRes3,userRes4));
		Mockito.when(uService.getAllUsers()).thenReturn(listOfUsers);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/v1/users").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(4)))
				.andExpect(jsonPath("$[2].lastName", is("Jeddy")));
		
		
	}
	
	@Test
	public void getAllUsers_NotFound() throws Exception {
	
		List<UserResponse> listOfUsers = new ArrayList<UserResponse>(Arrays.asList());
		Mockito.when(uService.getAllUsers()).thenReturn(listOfUsers);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/v1/users").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
		
		
	}
	
	@Test
	public void addUser() throws Exception{
		
		UserResponse uRes = UserResponse.builder().firstName("John").lastName("Campbell")
				.address("#567, Sector-16B, Chandigarh").pincode("160062").birthDate(LocalDate.of(1989, Month.MAY, 3))
				.joiningDate(LocalDate.of(2020, Month.JANUARY, 3)).userId(1).status(UserStatus.ACTIVE.name()).build();
		
		UserRequest uReq = UserRequest.builder().firstName("John").lastName("Campbell")
				.address("#567, Sector-16B, Chandigarh").pincode("160062").birthDate(LocalDate.of(1989, Month.MAY, 3))
				.joiningDate(LocalDate.of(2020, Month.JANUARY, 3)).build();
		
		Mockito.when(uService.addUser(uReq)).thenReturn(Optional.of(uRes));
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/users").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(this.mapper.writeValueAsString(uReq));
		
		mockMvc.perform(mockRequest).andExpect(status().isNoContent());
	
	}
	
	@Test
	public void deleteUser_success() throws Exception{
		Mockito.when(uService.deleteUser(1)).thenReturn(true);
		MockHttpServletRequestBuilder mcReq= MockMvcRequestBuilders.delete("/v1/users/1").contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(mcReq).andExpect(status().isOk());
			
	}
	
	@Test
	public void deleteUser_notFound() throws Exception{
		Mockito.when(uService.deleteUser(105)).thenReturn(false);
		MockHttpServletRequestBuilder mcReq= MockMvcRequestBuilders.delete("/v1/users/105").contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(mcReq).andExpect(status().isNotFound());
		
		
	}

	
	@Test
	public void searchByAnyOne_success() throws Exception{
	
		List<UserResponse> result = new  ArrayList<UserResponse>(Arrays.asList(userRes1));
			
		Mockito.when(uService.searchByAnyOne("Jacob", "", "160016")).thenReturn(result);
		
		MockHttpServletRequestBuilder mcReq = MockMvcRequestBuilders.get("/v1/users/find")
				.contentType(MediaType.APPLICATION_JSON).param("firstName", "Jacob").param("lastName", "")
				.param("pincode", "160016");
		mockMvc.perform(mcReq).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
		.andExpect(jsonPath("$[0].pincode", is("160016")));;
	}
	
	@Test
	public void searchByAnyOne_notFound() throws Exception{
	
		List<UserResponse> result = new  ArrayList<UserResponse>(Arrays.asList());
			
		Mockito.when(uService.searchByAnyOne("Mike", "", "160071")).thenReturn(result);
		
		MockHttpServletRequestBuilder mcReq = MockMvcRequestBuilders.get("/v1/users/find")
				.contentType(MediaType.APPLICATION_JSON).param("firstName", "Mike").param("lastName", "")
				.param("pincode", "160071");
		mockMvc.perform(mcReq).andExpect(status().isNoContent());
	}
	
}
