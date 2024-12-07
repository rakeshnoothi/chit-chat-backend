package com.rakeshnoothi.chit_chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatMessageInboundPrivateDTO {
	private String toUser;
	private String fromUser;
	private String message;
	private String id;
	private Boolean isSent;
}
