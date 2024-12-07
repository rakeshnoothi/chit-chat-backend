package com.rakeshnoothi.chit_chat.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.rakeshnoothi.chit_chat.dto.ChannelDTO;
import com.rakeshnoothi.chit_chat.dto.FriendDTO;
import com.rakeshnoothi.chit_chat.dto.UserDTO;
import com.rakeshnoothi.chit_chat.entity.User;
import com.rakeshnoothi.chit_chat.exception.UserNotFoundException;
import com.rakeshnoothi.chit_chat.repo.UserRepo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserRepo userRepo;
	
	public Set<FriendDTO> getUserFriends(Long userId){
		Optional<User> user = this.userRepo.findById(userId);
		if(!user.isPresent())throw new UserNotFoundException("User with the id " + userId + " does not exist");
		
		Set<FriendDTO> userFriends = this.userRepo.findFriendsByUserId(userId);
		return userFriends;
	}
	
	
	public UserDTO getUser(Long userId) {
		Optional<User> optionalUser = this.userRepo.findById(userId);
		
		if(!optionalUser.isPresent())throw new UserNotFoundException("User with the id " + userId + " does not exist");
		
		User user = optionalUser.get();
		
		UserDTO userDTO = UserDTO.builder()
				.id(user.getId())
				.firstname(user.getFirstname())
				.lastname(user.getLastname())
				.username(user.getUsername())
				.email(user.getEmail())
				.build();
		
		return userDTO;
	}
	
	public UserDTO getUserByUsername(String username) {
		Optional<User> optionalUser = this.userRepo.findByUsername(username);
		
		if(!optionalUser.isPresent())throw new UserNotFoundException("User with the username " + username + " does not exist");
		
		User user = optionalUser.get();
		
		UserDTO userDTO = UserDTO.builder()
				.id(user.getId())
				.firstname(user.getFirstname())
				.lastname(user.getLastname())
				.username(user.getUsername())
				.email(user.getEmail())
				.build();
		
		return userDTO;
	}
	
	public List<ChannelDTO> getUserChannels(Long userId) {
		List<ChannelDTO> userChannels = userRepo.findChannelsByUserId(userId);
		return userChannels;
	}
	
}
