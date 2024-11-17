package com.rakeshnoothi.chit_chat.filter;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rakeshnoothi.chit_chat.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
			
			String authorizationHeader = request.getHeader("Authorization");
			if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);
	            return;
			}
			
			// check if the authorization headers are provided but not the token.
			String token = authorizationHeader.substring(7);
			if(token == null) {
				/*
				 * forward the request and response to the filters ahead of this filter
				 * so they will decide whether to permit the request or not.
				 * */  
				filterChain.doFilter(request, response);
	            return;
			}
			
			try {
				String username = this.jwtUtil.getUsernameFromToken(token);
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (username != null && authentication == null) {
					UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					this.jwtUtil.isTokenValid(token, userDetails);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
				 
			}catch(ExpiredJwtException ee) {
				String jsonResponse = createJsonString(LocalDateTime.now(), 
									HttpStatus.UNAUTHORIZED.value(),
									"Token expired",
									""
								);
				 response.setContentType("application/json");
				 response.setCharacterEncoding("UTF-8");
				 response.setStatus(HttpStatus.UNAUTHORIZED.value());
				 response.getWriter().write(jsonResponse);
	             response.flushBuffer();
				return;
			}catch(Exception e) {
				String jsonResponse = createJsonString(LocalDateTime.now(), 
									HttpStatus.BAD_REQUEST.value(),
									"Invalid token",
									""
								);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				response.getWriter().write(jsonResponse);
				response.flushBuffer();
				return;
			}
			 
			filterChain.doFilter(request, response);
			return;
	}
	
	private String createJsonString(LocalDateTime timestamp, Integer statusCode, String message, Object body) {
	    String bodyString = body != null ? body.toString() : "{}"; // Converts the body to string or an empty JSON if null
	    return String.format(
	        "{\"timestamp\":\"%s\",\"statusCode\":%d,\"message\":\"%s\",\"body\":%s}",
	        timestamp, statusCode, message, bodyString
	    );
	}


}
