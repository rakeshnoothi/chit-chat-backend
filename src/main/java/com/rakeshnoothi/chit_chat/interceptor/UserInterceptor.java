package com.rakeshnoothi.chit_chat.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.rakeshnoothi.chit_chat.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class UserInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
			if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
				// TODO: Respond with STOMP error message
				return message;
			}
			
			// check if the authorization headers are provided but not the token.
			String token = authorizationHeader.substring(7);
			if (token == null) {
				// TODO: Respond with STOMP error message
				return message;
			}
			
			try {
				System.out.println(userDetailsService);
				String username = this.jwtUtil.getUsernameFromToken(token);
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (username != null && authentication == null) {
					UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					this.jwtUtil.isTokenValid(token, userDetails);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					accessor.setUser(authenticationToken);
				}

			} catch (ExpiredJwtException ee) {
				// TODO: Respond with STOMP error message
				return message;
			} catch (Exception e) {
				System.out.println(e);
				// TODO: Respond with STOMP error message
				return message;
			}
		}

		return message;
	}

}
