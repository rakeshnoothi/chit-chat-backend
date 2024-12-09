package com.rakeshnoothi.chit_chat.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.rakeshnoothi.chit_chat.dto.ChatMessageOutboundPrivateDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WaitingMessages {
	private final Map<String, List<ChatMessageOutboundPrivateDTO>> usersMessages = new HashMap<>();
	
	public void addMessage(String username, ChatMessageOutboundPrivateDTO message) {
		usersMessages.computeIfAbsent(username, (k) -> {
			List<ChatMessageOutboundPrivateDTO> messages = new ArrayList<>();
			return messages;
		}).add(message);
		log.info("Added message to the waiting messages for {}", username);
	}
	
	public void removeMessages(String username) {
		usersMessages.remove(username);
	}
	
	public boolean areMessagesAvailable(String username) {
		if(usersMessages.containsKey(username)) return true;
		return false;
	}
	
	public List<ChatMessageOutboundPrivateDTO> getUserMessages(String username){
		return usersMessages.get(username);
	}
}
