package com.rakeshnoothi.chit_chat.event;

import java.security.Principal;
import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.rakeshnoothi.chit_chat.dto.ChatMessageOutboundPrivateDTO;
import com.rakeshnoothi.chit_chat.util.OnlineUsers;
import com.rakeshnoothi.chit_chat.util.WaitingMessages;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SessionConnectedEventListener {
	
	private final OnlineUsers onlineUsers;
	private final WaitingMessages waitingMessages;
	private final SimpMessagingTemplate simpMessagingTemplate;
	
	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		Principal connectedUser = event.getUser();
		String connectedUsername = connectedUser.getName();
		onlineUsers.add(connectedUsername);
	}
	
	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent  event) {
		Principal connectedUser = event.getUser();
		String disconnectedUsername = connectedUser.getName();
		onlineUsers.remove(disconnectedUsername);
	}
	
	@EventListener
	public void handleSessionSubscribeEventListener(SessionSubscribeEvent  event) {
		Principal connectedUser = event.getUser();
		String connectedUsername = connectedUser.getName();
		
		
		if(waitingMessages.areMessagesAvailable(connectedUsername)) {
			List<ChatMessageOutboundPrivateDTO> userMessages = waitingMessages.getUserMessages(connectedUsername);
			userMessages.forEach((message) -> {
				System.out.println("Message for other person: " + message.getMessage());
				simpMessagingTemplate.convertAndSendToUser(connectedUsername, "/queue/private/messages", message);
			});
			waitingMessages.removeMessages(connectedUsername);
		}
	}
}
