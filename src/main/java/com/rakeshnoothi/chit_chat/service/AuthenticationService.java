package com.rakeshnoothi.chit_chat.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rakeshnoothi.chit_chat.dto.UserDTO;
import com.rakeshnoothi.chit_chat.dto.UserLoginRequestDTO;
import com.rakeshnoothi.chit_chat.dto.UserLoginResponseDTO;
import com.rakeshnoothi.chit_chat.dto.UserRegistrationRequestDTO;
import com.rakeshnoothi.chit_chat.entity.User;
import com.rakeshnoothi.chit_chat.repo.UserRepo;
import com.rakeshnoothi.chit_chat.util.JwtUtil;

@Service
public class AuthenticationService {

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserRepo userRepo;

	@Autowired
	private AuthenticationManager authenticationManager;

	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public void registerNewUser(UserRegistrationRequestDTO userRegistrationRequestDTO) {
		User newUser = User.builder()
				.firstname(userRegistrationRequestDTO.getFirstname())
				.lastname(userRegistrationRequestDTO.getLastname())
				.username(userRegistrationRequestDTO.getUsername())
				.email(userRegistrationRequestDTO.getEmail())
				.password(encoder.encode(userRegistrationRequestDTO.getPassword()))
				.enabled(true)
				.build();

		try {
			this.userRepo.save(newUser);
		} catch (DataIntegrityViolationException ex) {
			throw new DataIntegrityViolationException("User with the username or email already exists");
		}
	}
	
	public UserLoginResponseDTO loginUser(UserLoginRequestDTO userLoginRequestDTO) {
		String username = userLoginRequestDTO.getUsername();
		String password = userLoginRequestDTO.getPassword();
		
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		
		try {
			/* Authenticates the user by checking the credentials against credentials 
			 * in the database. This method makes a call to the database */
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			User user = (User) authentication.getPrincipal();
			
			String jwtToken = this.jwtUtil.buildJwtToken(user.getUsername());
			
			UserDTO userDTO = UserDTO.builder()
								.id(user.getId())
								.firstname(user.getFirstname())
								.lastname(user.getLastname())
								.username(user.getUsername())
								.email(user.getEmail())
								.build();
								
			
			UserLoginResponseDTO userLoginResponseDTO = UserLoginResponseDTO
												.builder()
												.token(jwtToken)
												.tokenType("Bearer")
												.expiresMs(jwtUtil.getExpiresInMs())
												.issuedAt(LocalDateTime.now())
												.user(userDTO)
												.build();
			return userLoginResponseDTO;
			
		}catch(BadCredentialsException e) {
			throw new BadCredentialsException("Invalid username or password");
		}
	}
}
