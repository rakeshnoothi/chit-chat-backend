package com.rakeshnoothi.chit_chat.util;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OnlineUsers {
	
	private final Set<String> users = new HashSet<>();
	
	public void add(String username) {
		log.info("{} connected", username);
		users.add(username);
	}
	
	public void remove(String username) {
		log.info("{} disconnected", username);
		users.remove(username);
	}
	
	public boolean isOnline(String username) {
		if(users.contains(username))return true;
		return false;
	}
}
