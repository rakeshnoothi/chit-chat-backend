package com.rakeshnoothi.chit_chat.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rakeshnoothi.chit_chat.entity.User;
import com.rakeshnoothi.chit_chat.repo.UserRepo;


@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserRepo userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUsername(username);
		if (!user.isPresent()) {
			throw new UsernameNotFoundException("User with the provided username " + username + " does not exist");
		}
		return user.get();
	}

}
