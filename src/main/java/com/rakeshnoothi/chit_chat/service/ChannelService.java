package com.rakeshnoothi.chit_chat.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.rakeshnoothi.chit_chat.dto.CreateNewChannelDTO;
import com.rakeshnoothi.chit_chat.entity.Channel;
import com.rakeshnoothi.chit_chat.entity.User;
import com.rakeshnoothi.chit_chat.exception.DuplicateDataException;
import com.rakeshnoothi.chit_chat.exception.NotFoundException;
import com.rakeshnoothi.chit_chat.exception.UserNotFoundException;
import com.rakeshnoothi.chit_chat.repo.ChannelRepo;
import com.rakeshnoothi.chit_chat.repo.UserRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChannelService {
	
	public final ChannelRepo channelRepo;
	public final UserRepo userRepo;
	
	@Transactional
	public void createNewChannel(CreateNewChannelDTO createNewChannelDTO) {
		//TODO: perform necessary checks.
		
		Optional<User> optionalFetchedUser = this.userRepo.findById(createNewChannelDTO.getCreatedByUserId());
		if(!optionalFetchedUser.isPresent()) {
			throw new UserNotFoundException("User with the provided user id does not exist");
		}
		
		Channel newChannel = Channel.builder()
								.name(createNewChannelDTO.getName())
								.createdAt(LocalDateTime.now())
								.activeMembers(1)
								.totalMembers(1)
								.build();
		
		try {
			User fetchedUser = optionalFetchedUser.get();
			Set<Channel> channels= new HashSet<>();
			channels.add(newChannel);
			fetchedUser.setChannels(channels);
			
			List<User> members = new ArrayList<>();
			members.add(fetchedUser);
			newChannel.setMembers(members);
			
			this.userRepo.save(fetchedUser);
		} catch (DataIntegrityViolationException ex) {
			throw new DataIntegrityViolationException("Channel with the provided name already exits");
		}
	}
	
	@Transactional
	public void addNewMemberToChannel(Long channelId, Long adderUserId, Long toBeAddedUserId) {
		// Only the members in the channel can add other members.
		Optional<Channel> optionalFetchedChannel = this.channelRepo.findById(channelId);
		if(!optionalFetchedChannel.isPresent()) {
			throw new NotFoundException("The provided channel does not exist");
		}
		
		boolean doesUserExistInChannel = this.channelRepo.existsByChannelIdAndUserId(channelId, adderUserId);
		if(!doesUserExistInChannel) {
			throw new UserNotFoundException("adding user does not exist in the channel");
		}
		
		Optional<User> optionalFetchedToBeAddedUser = this.userRepo.findById(toBeAddedUserId);
		if(!optionalFetchedToBeAddedUser.isPresent()) {
			throw new UserNotFoundException("The user you are trying to add does not exist");
		}
		
		boolean doesToBeAddedUserExistInChannel = this.channelRepo.existsByChannelIdAndUserId(channelId, toBeAddedUserId);
		if(doesToBeAddedUserExistInChannel) {
			throw new DuplicateDataException("The user you are trying to add already in the channel");
		}
		
		Channel fetchedChannel = optionalFetchedChannel.get();
		User fetchedToBeAddedUser = optionalFetchedToBeAddedUser.get();
		
		fetchedChannel.getMembers().add(fetchedToBeAddedUser);
		if(fetchedToBeAddedUser.getChannels() != null) {
			fetchedToBeAddedUser.getChannels().add(fetchedChannel);
		}else {
			Set<Channel> channels= new HashSet<>();
			channels.add(fetchedChannel);
			fetchedToBeAddedUser.setChannels(channels);
		}
		fetchedChannel.setTotalMembers(fetchedChannel.getTotalMembers() + 1);  
		this.userRepo.save(fetchedToBeAddedUser);
		
	}
}
