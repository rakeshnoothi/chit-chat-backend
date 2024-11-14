package com.rakeshnoothi.chit_chat.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rakeshnoothi.chit_chat.dto.FriendRequestDTO;
import com.rakeshnoothi.chit_chat.service.FriendRequestService;
import com.rakeshnoothi.chit_chat.util.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/friend-request")
public class FriendRequestController {
	
	private final FriendRequestService friendRequestService;
	
	@PostMapping("/request")
	public ResponseEntity<SuccessResponse> sendFriendRequest(@RequestParam Long senderId, @RequestParam String receiverUsername){
		this.friendRequestService.addFriendRequest(senderId, receiverUsername);
		SuccessResponse response = SuccessResponse.builder()
									.statusCode(HttpStatus.OK.value())
									.message("Friend request sent successfully")
									.body(null)
									.build();
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/received/{receiverId}")
	public ResponseEntity<SuccessResponse> getFriendRequestsReceived(@PathVariable Long receiverId){
		List<FriendRequestDTO> friendRequestsReceived = this.friendRequestService.getFriendRequestsReceived(receiverId);
		SuccessResponse response = SuccessResponse.builder()
									.statusCode(HttpStatus.OK.value())
									.message("Friend requests received")
									.body(friendRequestsReceived)
									.build();
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/sent/{senderId}")
	public ResponseEntity<SuccessResponse> getFriendRequestsSent(@PathVariable Long senderId){
		List<FriendRequestDTO> friendRequestsReceived = this.friendRequestService.getFriendRequestsSent(senderId);
		SuccessResponse response = SuccessResponse.builder()
									.statusCode(HttpStatus.OK.value())
									.message("Friend requests sent")
									.body(friendRequestsReceived)
									.build();
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/action")
	public ResponseEntity<SuccessResponse> takeActionOnFriendRequest(@RequestParam Long requestId, @RequestParam String type){
		String message = this.friendRequestService.takeActionOnFriendRequest(requestId, type);
		SuccessResponse response = SuccessResponse.builder()
									.statusCode(HttpStatus.OK.value())
									.message(message)
									.body(null)
									.build();
		return ResponseEntity.ok(response);
	}
	
}
