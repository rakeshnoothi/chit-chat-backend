package com.rakeshnoothi.chit_chat.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ChatMessageOutboundChannelDTO {
	private String fromUser, message;
	
	private Long channelId;
	
	private Boolean isSent;
	
	private String id;
	
	@Builder.Default
	private LocalDateTime timeStamp = LocalDateTime.now();
}
