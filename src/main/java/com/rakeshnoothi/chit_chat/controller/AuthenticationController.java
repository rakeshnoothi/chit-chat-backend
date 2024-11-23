package com.rakeshnoothi.chit_chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rakeshnoothi.chit_chat.dto.UserLoginRequestDTO;
import com.rakeshnoothi.chit_chat.dto.UserLoginResponseDTO;
import com.rakeshnoothi.chit_chat.dto.UserRegistrationRequestDTO;
import com.rakeshnoothi.chit_chat.service.AuthenticationService;
import com.rakeshnoothi.chit_chat.util.SuccessResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping("/register")
	public ResponseEntity<SuccessResponse> registerNewUser(@Valid @RequestBody UserRegistrationRequestDTO userRegistrationRequestDTO) {
		this.authenticationService.registerNewUser(userRegistrationRequestDTO);
		SuccessResponse response = SuccessResponse.builder()
									.statusCode(HttpStatus.OK.value())
									.message("New user registered successfully")
									.body("")
									.build();
		return ResponseEntity.ok(response);	
    }
	
	@PostMapping("/login")
	public ResponseEntity<SuccessResponse> login(@Valid @RequestBody UserLoginRequestDTO userLoginRequestDTO){
		UserLoginResponseDTO userLoginResponseDTO = this.authenticationService.loginUser(userLoginRequestDTO);
		SuccessResponse response = SuccessResponse.builder()
									.statusCode(HttpStatus.OK.value())
									.message("User logged in successfully")
									.body(userLoginResponseDTO)
									.build();
		return ResponseEntity.ok(response);	
	}
}
