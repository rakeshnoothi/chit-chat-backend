package com.rakeshnoothi.chit_chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.rakeshnoothi.chit_chat.dto.ChatMessageInboundDTO;
import com.rakeshnoothi.chit_chat.dto.ChatMessageOutboundDTO;

@Controller
public class MessageController {
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@MessageMapping("/private/message")
	public void sendPrivateMessage(ChatMessageInboundDTO inboundMessage) {
		ChatMessageOutboundDTO outBoundMessage = ChatMessageOutboundDTO.builder()
			.fromUser(inboundMessage.getFromUser())
			.toUser(inboundMessage.getToUser())
			.message(inboundMessage.getMessage())
			.build();
		simpMessagingTemplate.convertAndSendToUser(inboundMessage.getToUser(), "/queue/private/messages", outBoundMessage);
	}
	
	@MessageMapping("/channel/message")
	@SendTo("/topic/channel/messages")
	public String broadcastMessage(ChatMessageInboundDTO message) {
	    return "Hello from server: " + message.getMessage();
	}
}
