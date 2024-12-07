package com.rakeshnoothi.chit_chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rakeshnoothi.chit_chat.dto.CreateNewChannelDTO;
import com.rakeshnoothi.chit_chat.service.ChannelService;
import com.rakeshnoothi.chit_chat.util.SuccessResponse;

@RestController
@RequestMapping("/api/v1/channel")
public class ChannelController {
	private final ChannelService channelService;
	
	public ChannelController(ChannelService channelService) {
		this.channelService = channelService;
	}
	
	@PostMapping("/create")
	public ResponseEntity<SuccessResponse> createNewChannel(@RequestBody CreateNewChannelDTO createNewChannelDTO){
		this.channelService.createNewChannel(createNewChannelDTO);
		SuccessResponse response = SuccessResponse.builder()
									.statusCode(HttpStatus.OK.value())
									.message("New Channel created successfully")
									.body(null)
									.build();
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/addUser")
	public ResponseEntity<SuccessResponse> addNewUserToChannel(@RequestParam Long channelId, @RequestParam Long adderUserId, @RequestParam Long toBeAddedUserId){
		this.channelService.addNewMemberToChannel(channelId, adderUserId, toBeAddedUserId);
		SuccessResponse response = SuccessResponse.builder()
									.statusCode(HttpStatus.OK.value())
									.message("Added new member to the channel successfully")
									.body(null)
									.build();
		return ResponseEntity.ok(response);
	}
}
