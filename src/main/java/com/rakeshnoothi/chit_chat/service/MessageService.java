package com.rakeshnoothi.chit_chat.service;

import java.util.Set;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.rakeshnoothi.chit_chat.dto.ChatMessageInboundChannelDTO;
import com.rakeshnoothi.chit_chat.dto.ChatMessageInboundPrivateDTO;
import com.rakeshnoothi.chit_chat.dto.ChatMessageOutboundChannelDTO;
import com.rakeshnoothi.chit_chat.dto.ChatMessageOutboundPrivateDTO;
import com.rakeshnoothi.chit_chat.repo.ChannelRepo;
import com.rakeshnoothi.chit_chat.util.OnlineUsers;
import com.rakeshnoothi.chit_chat.util.WaitingMessages;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MessageService {
	
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final ChannelRepo channelRepo;
	private final OnlineUsers onlineUsers;
	private final WaitingMessages waitingMessages;
	
	public void SendPrivateMessage(ChatMessageInboundPrivateDTO chatMessageInboundPrivateDTO) {
		ChatMessageOutboundPrivateDTO outBoundMessage = ChatMessageOutboundPrivateDTO.builder()
				.fromUser(chatMessageInboundPrivateDTO.getFromUser())
				.toUser(chatMessageInboundPrivateDTO.getToUser())
				.message(chatMessageInboundPrivateDTO.getMessage())
				.id(chatMessageInboundPrivateDTO.getId())
				.isSent(chatMessageInboundPrivateDTO.getIsSent())
				.build();
		
		if(onlineUsers.isOnline(chatMessageInboundPrivateDTO.getToUser())) {
			simpMessagingTemplate.convertAndSendToUser(chatMessageInboundPrivateDTO.getToUser(), "/queue/private/messages", outBoundMessage);
			return;
		}
		waitingMessages.addMessage(chatMessageInboundPrivateDTO.getToUser(), outBoundMessage);
		return;
	}
	
	public void sendChannelMessage(ChatMessageInboundChannelDTO chatMessageInboundChannelDTO) {
		System.out.println("channel message: " + chatMessageInboundChannelDTO);
		Set<String> usernamesInChannel = channelRepo.findUsernamesByChannelId(chatMessageInboundChannelDTO.getChannelId());
		
		ChatMessageOutboundChannelDTO outBoundMessage = ChatMessageOutboundChannelDTO.builder()
				.fromUser(chatMessageInboundChannelDTO.getFromUser())
				.message(chatMessageInboundChannelDTO.getMessage())
				.channelId(chatMessageInboundChannelDTO.getChannelId())
				.isSent(chatMessageInboundChannelDTO.getIsSent())
				.id(chatMessageInboundChannelDTO.getId())
				.build();
		
		// Send message to every user in the set.
		usernamesInChannel.forEach(username -> {
			if(!username.equals(chatMessageInboundChannelDTO.getFromUser())) {
				simpMessagingTemplate.convertAndSendToUser(username, "/queue/channel/messages", outBoundMessage);
			}
		});
		
	}
}
