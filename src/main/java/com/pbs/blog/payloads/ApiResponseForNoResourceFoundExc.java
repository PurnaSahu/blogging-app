package com.pbs.blog.payloads;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseForNoResourceFoundExc {

	private String message;
	private HttpStatusCode httpStatus;
	private String path;
	private ProblemDetail detailMessage;
	//private HttpMethod httpMethod;
}
