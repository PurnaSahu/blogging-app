package com.pbs.blog.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.pbs.blog.payloads.ApiResponse;
import com.pbs.blog.payloads.ApiResponseForNoResourceFoundExc;

//@ControllerAdvice -> as we are working with rest api so we will use
@RestControllerAdvice
public class GlobalExceptionHandler {

	/*whenever there is a specific type of excetion will occur, we will handle that exception here
	   whenever there is any exception occurs, the respective handler method will execute.
	 */
	
	/***************************************1st Exception*********************************************/
	
	/*Note: this resoucenotfoundexception.class is a custom exception we created, its not inbuilt class. when this error occurs
	 * we will handle with proper message*/
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
		/*
		   when this method executes, we will get all the data in "ex" object and we can get() all those properties and create those
		   properties in ApiResponse class and pass the data to that and show all the messages to client if needed 
		   
		   right now i'm only fetching message
		 */
		String errorMessage = ex.getMessage();
		ApiResponse response = new ApiResponse(errorMessage, false);
		return new ResponseEntity<ApiResponse>(response,HttpStatus.NOT_FOUND);
		//return new ResponseEntity<ApiResponse>(new ApiResponse(ex.getMessage(),false),HttpStatus.METHOD_FAILURE);
	}
	
	/***************************************2nd Exception*********************************************/
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
		/*Here we are returning Map,
		 Coz, if user data we passing has 4 properties like; name, mail, password & about then we dont know which field will get 
		 which type of validation exception.if all the 4 properties having some validation issues then will keep all 4 error 
		 messages in our map
		 */
		Map<String, String> response = new HashMap<>(); // dummy map created
		
		//getAllErrors() -> will return list of object error, now from each object error we will fetch fields and its message.
		//ToDo-> fetch many such fields from error object and show them
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError)error).getField();
			String message = error.getDefaultMessage();
			
			response.put(fieldName, message);
		});
		
		return new ResponseEntity<Map<String,String>>(response, HttpStatus.BAD_REQUEST);
	}
	
	/***************************************3rd Exception*********************************************/
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ApiResponse>  handlingWrongApiAddress(HttpRequestMethodNotSupportedException err){

		/*error body messageProblemDetail[type='about:blank', title='Method Not Allowed', status=405, detail='Method 'DELETE' is not supported.', instance='null', properties='null']
			Status code is: 405 METHOD_NOT_ALLOWED	*/
		
		ProblemDetail errorBody = err.getBody();
		String message= errorBody.getDetail() + ". This URL is incomplete/wrong, Please chack again !!";
		err.getStatusCode();
		//System.out.println("error body message"+errorBody);
		//System.out.println("Status code is: "+err.getStatusCode());

		return new ResponseEntity<ApiResponse> (new ApiResponse(message, false),HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	/***************************************4th Exception*********************************************/
	
	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ApiResponseForNoResourceFoundExc>  resourceNotAvailable(NoResourceFoundException x){
		
		ProblemDetail errorBody = x.getBody();
		
		HttpStatusCode status= x.getStatusCode();
		//String httpMethod = x.getHttpMethod().values();
		String message2 = "No such API found in the Application, Please Check again !!" ;
				//x.getMessage();
		String resourcePath = x.getResourcePath();
		//System.out.println("error body message"+errorBody);
		//System.out.println("Status code is: "+status+" default message is"+message2+"path is:"+resourcePath);

		return new ResponseEntity<ApiResponseForNoResourceFoundExc> 
					(new ApiResponseForNoResourceFoundExc(
							message2, 
							status, 
							resourcePath,
							errorBody
							),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(MissingPathVariableException.class)
	public ResponseEntity<ApiResponseForNoResourceFoundExc>  wrongParameterPassedInApi(MissingPathVariableException x){
		
		ProblemDetail errorBody = x.getBody();
		
		HttpStatusCode status= x.getStatusCode();
		//String httpMethod = x.getHttpMethod().values();
		String customMessage = "The parameters passed in API seems wrong or invalid Type, Please check again !!" ;
		String variableName= x.getVariableName();
		//System.out.println("error body message"+errorBody);
		//System.out.println("Status code is: "+status+" default message is"+message2+"path is:"+resourcePath);

		return new ResponseEntity<ApiResponseForNoResourceFoundExc> 
					(new ApiResponseForNoResourceFoundExc(
							customMessage, 
							status, 
							variableName,
							errorBody
							),HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
