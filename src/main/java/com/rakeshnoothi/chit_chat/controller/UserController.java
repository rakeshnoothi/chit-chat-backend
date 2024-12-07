package com.rakeshnoothi.chit_chat.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rakeshnoothi.chit_chat.dto.ChannelDTO;
import com.rakeshnoothi.chit_chat.dto.FriendDTO;
import com.rakeshnoothi.chit_chat.dto.UserDTO;
import com.rakeshnoothi.chit_chat.service.UserService;
import com.rakeshnoothi.chit_chat.util.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
	
	private final UserService userService;
	
	@GetMapping("/friends/{userId}")
	public ResponseEntity<SuccessResponse> getUserFriends(@PathVariable Long userId){
		Set<FriendDTO> userFriends = this.userService.getUserFriends(userId);
		SuccessResponse response = SuccessResponse.builder()
									.statusCode(HttpStatus.OK.value())
									.message("Fetched user Friends successfully")
									.body(userFriends)
									.build();
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/id/{userId}")
	public ResponseEntity<SuccessResponse> getUserById(@PathVariable Long userId){
		UserDTO userDTO = this.userService.getUser(userId);
		SuccessResponse response = SuccessResponse.builder()
									.statusCode(HttpStatus.OK.value())
									.message("Fetched user Friends successfully")
									.body(userDTO)
									.build();
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/username/{username}")
	public ResponseEntity<SuccessResponse> getUserByUsername(@PathVariable String username){
		UserDTO userDTO = this.userService.getUserByUsername(username);
		SuccessResponse response = SuccessResponse.builder()
									.statusCode(HttpStatus.OK.value())
									.message("Fetched user Friends successfully")
									.body(userDTO)
									.build();
		return ResponseEntity.ok(response);
	}
	
	
	@GetMapping("/channels/{userId}")
	public ResponseEntity<SuccessResponse> getUserChannels(@PathVariable Long userId){
		List<ChannelDTO> userChannels = this.userService.getUserChannels(userId);
		SuccessResponse response = SuccessResponse.builder()
									.statusCode(HttpStatus.OK.value())
									.message("Fetched channels successfully")
									.body(userChannels)
									.build();
		return ResponseEntity.ok(response);
	}
}
