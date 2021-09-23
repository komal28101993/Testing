package com.poc.student.request;

import com.sun.istack.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProjectRequest {

	@NotNull
	private String projectName;
	
	@NotNull
	private String duration;
}
