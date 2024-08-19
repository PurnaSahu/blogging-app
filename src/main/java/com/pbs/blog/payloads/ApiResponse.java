package com.pbs.blog.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

	private String message;
	private boolean success;
	/*
	 in Response, if we want to pass other properties then we cann add here in future. like;
	 - number of records effected in DB
	 - result from DB or data which affected in DB
	 - httpstatus
	 */
}
