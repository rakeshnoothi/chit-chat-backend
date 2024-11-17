package com.rakeshnoothi.chit_chat.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
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
	 @Qualifier("clientOutboundChannel")
	 private MessageChannel clientOutboundChannel;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;
    
    private StompHeaderAccessor accessor;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		this.accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

		if (StompCommand.CONNECT.equals(this.accessor.getCommand())) {
			String authorizationHeader = this.accessor.getFirstNativeHeader("Authorization");
			if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
				this.sendErrorMessage("Invalid authorization header or null");
				return null;
			}
			
			// check if the authorization headers are provided but not the token.
			String token = authorizationHeader.substring(7);
			if (token == null) {
				this.sendErrorMessage("Empty token");
				return null;
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
					this.accessor.setUser(authenticationToken);
				}

			} catch (ExpiredJwtException ee) {
				this.sendErrorMessage("Token expired");
				return null;
			} catch (Exception e) {
				this.sendErrorMessage("Invalid token");
				return null;
			}
		}

		return message;
	}
	
	private void sendErrorMessage(String message) {
		StompHeaderAccessor errorHeaderAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
		errorHeaderAccessor.setMessage(message);
		errorHeaderAccessor.setSessionId(this.accessor.getSessionId());
		this.clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0], errorHeaderAccessor.getMessageHeaders()));
	}

}
