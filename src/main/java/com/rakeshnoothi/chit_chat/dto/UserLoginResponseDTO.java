package com.rakeshnoothi.chit_chat.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginResponseDTO {
	private String token;
	private String tokenType;
	private Long expiresMs;
	private LocalDateTime issuedAt;
	private UserDTO user;
}
