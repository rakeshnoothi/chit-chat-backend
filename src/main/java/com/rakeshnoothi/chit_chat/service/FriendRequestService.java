package com.rakeshnoothi.chit_chat.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rakeshnoothi.chit_chat.dto.FriendRequestDTO;
import com.rakeshnoothi.chit_chat.entity.FriendRequest;
import com.rakeshnoothi.chit_chat.entity.User;
import com.rakeshnoothi.chit_chat.enums.RequestStatus;
import com.rakeshnoothi.chit_chat.exception.DuplicateDataException;
import com.rakeshnoothi.chit_chat.exception.NotFoundException;
import com.rakeshnoothi.chit_chat.exception.UserNotFoundException;
import com.rakeshnoothi.chit_chat.repo.FriendRequestRepo;
import com.rakeshnoothi.chit_chat.repo.UserRepo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FriendRequestService {
	private final FriendRequestRepo friendRequestRepo;
	private final UserRepo userRepo;
	
	public void addFriendRequest(Long senderId, String receiverUsername) {
		Optional<User> sender = this.userRepo.findById(senderId);
		Optional<User> receiver = this.userRepo.findByUsername(receiverUsername);
		
		//check if the sender and receiver exists in the database
		if(!sender.isPresent()) {
			throw new UserNotFoundException("User with the ID " + senderId + " does not exist");
		}else if(!receiver.isPresent()) {
			throw new UserNotFoundException("User with the username " + receiverUsername + " does not exist");
		}
		
		//check if user sending request to himself
		if(sender.get().getUsername().equals(receiverUsername)) {
			throw new IllegalArgumentException("Cannot send friend request to yourself");
		}
		
		// check if a request already exists in the database.
		if(this.friendRequestRepo.existsBySenderAndReceiver(sender.get(), receiver.get())) {
			throw new DuplicateDataException("Friend Request already sent");
		}
		
		//check if sender and receiver are already friends.
		if(this.userRepo.existsFriendship(senderId, receiver.get().getId())) {
			throw new DuplicateDataException("sender with the ID " + senderId + " and receiver with the ID " + receiver.get().getId() + " are already friends");
		}
		
		// create a new request and save
		FriendRequest friendRequest = FriendRequest.builder()
										.sender(sender.get())
										.receiver(receiver.get())
										.status(RequestStatus.PENDING)
										.build();

		this.friendRequestRepo.save(friendRequest);
	}
	
	public List<FriendRequestDTO> getFriendRequestsReceived(Long receiverId){
		// Check if the user with the provided ID exists
		Optional<User> user = this.userRepo.findById(receiverId);
		if(!user.isPresent())throw new UserNotFoundException("User with the Id " + receiverId + " does not exist");
		return this.friendRequestRepo.findFriendRequestByReceiverId(receiverId);
	}
	
	public List<FriendRequestDTO> getFriendRequestsSent(Long senderId){
		// Check if the user with the provided ID exists
		Optional<User> user = this.userRepo.findById(senderId);
		if(!user.isPresent())throw new UserNotFoundException("User with the Id " + senderId + " does not exist");
		return this.friendRequestRepo.findFriendRequestBySenderId(senderId);
	}
	
	
	@Transactional
	public String takeActionOnFriendRequest(Long requestId, String type) {
		if (type == null || type.isBlank()) {
	        throw new IllegalArgumentException("Please provide a valid action");
	    }
		
		Optional<FriendRequest> optionalFriendRequest  = this.friendRequestRepo.findById(requestId);
		if(!optionalFriendRequest.isPresent())throw new NotFoundException("There is no such friend request with the id " + requestId);
		
		FriendRequest friendRequest = optionalFriendRequest.get();
		
		User user1 = this.userRepo.findById(friendRequest.getSender().getId()).get();
		User user2 = this.userRepo.findById(friendRequest.getReceiver().getId()).get();
		
		if("accept".equalsIgnoreCase(type)) {
			friendRequest.setStatus(RequestStatus.ACCEPTED);
			this.friendRequestRepo.save(friendRequest);
			
			// add user1 to user2 friends list and vice-versa
			user1.getFriends().add(user2);
			user2.getFriends().add(user1);
			
			this.userRepo.save(user1);
			this.userRepo.save(user2);
			
			this.friendRequestRepo.deleteById(requestId);
			
			return "Friend request accepted";
		}else if("reject".equalsIgnoreCase(type)) {
			this.friendRequestRepo.deleteById(requestId);
			return "Friend request rejected";
		}else {
			throw new IllegalArgumentException("Please provide valid action");
		}
	}
}
