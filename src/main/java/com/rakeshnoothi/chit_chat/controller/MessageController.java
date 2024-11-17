package com.rakeshnoothi.chit_chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.rakeshnoothi.chit_chat.dto.ChatMessageInboundDTO;

public class MessageController {
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@MessageMapping("/private/message")
	public void sendPrivateMessage(ChatMessageInboundDTO message) {
		String toUser = message.getToUser();
		simpMessagingTemplate.convertAndSendToUser(toUser, "/queue/private/messages", message);
	}
	
	@MessageMapping("/channel/message")
	@SendTo("/topic/channel/messages")
	public String broadcastMessage(ChatMessageInboundDTO message) {
	    return "Hello from server: " + message.getMessage();
	}
}
