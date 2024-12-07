package com.rakeshnoothi.chit_chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.rakeshnoothi.chit_chat.dto.ChatMessageInboundChannelDTO;
import com.rakeshnoothi.chit_chat.dto.ChatMessageInboundPrivateDTO;
import com.rakeshnoothi.chit_chat.service.MessageService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MessageController {
	
	private final MessageService messageService;
	
	@MessageMapping("/private/message")
	public void sendPrivateMessage(ChatMessageInboundPrivateDTO inboundMessage) {
		messageService.SendPrivateMessage(inboundMessage);
	}
	
	@MessageMapping("/channel/message")
	public void sendChannelMessage(ChatMessageInboundChannelDTO chatMessageInboundChannelDTO) {
		messageService.sendChannelMessage(chatMessageInboundChannelDTO);
	}
	
}
