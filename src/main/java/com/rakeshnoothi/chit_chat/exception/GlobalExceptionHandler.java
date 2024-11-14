package com.rakeshnoothi.chit_chat.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rakeshnoothi.chit_chat.util.SuccessResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<SuccessResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
		SuccessResponse response = SuccessResponse.builder()
									.statusCode(HttpStatus.BAD_REQUEST.value())
									.message(ex.getFieldError().getDefaultMessage())
									.body(null)
									.build();
		return new ResponseEntity<SuccessResponse>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<SuccessResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex){
		SuccessResponse response = SuccessResponse.builder()
									.statusCode(HttpStatus.CONFLICT.value())
									.message(ex.getMessage())
									.body(null)
									.build();
		return new ResponseEntity<SuccessResponse>(response, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(DuplicateDataException.class)
	public ResponseEntity<SuccessResponse> handleDuplicateDataException(DuplicateDataException ex){
		SuccessResponse response = SuccessResponse.builder()
									.statusCode(HttpStatus.CONFLICT.value())
									.message(ex.getMessage())
									.body(null)
									.build();
		return new ResponseEntity<SuccessResponse>(response, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<SuccessResponse> handleUserNotFoundException(UserNotFoundException ex){
		SuccessResponse response = SuccessResponse.builder()
									.statusCode(HttpStatus.BAD_REQUEST.value())
									.message(ex.getMessage())
									.body(null)
									.build();
		return new ResponseEntity<SuccessResponse>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<SuccessResponse> handleIllegalArgumentException(IllegalArgumentException ex){
		SuccessResponse response = SuccessResponse.builder()
									.statusCode(HttpStatus.BAD_REQUEST.value())
									.message(ex.getMessage())
									.body(null)
									.build();
		return new ResponseEntity<SuccessResponse>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<SuccessResponse> handleNotFoundException(NotFoundException ex){
		SuccessResponse response = SuccessResponse.builder()
									.statusCode(HttpStatus.BAD_REQUEST.value())
									.message(ex.getMessage())
									.body(null)
									.build();
		return new ResponseEntity<SuccessResponse>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<SuccessResponse> handleBadCredentialsException(BadCredentialsException ex){
		SuccessResponse response = SuccessResponse.builder()
									.statusCode(HttpStatus.UNAUTHORIZED.value())
									.message(ex.getMessage())
									.body(null)
									.build();
		return new ResponseEntity<SuccessResponse>(response, HttpStatus.UNAUTHORIZED);
	}
}
