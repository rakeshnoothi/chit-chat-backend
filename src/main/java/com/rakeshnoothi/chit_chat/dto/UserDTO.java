package com.rakeshnoothi.chit_chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
	private Long id;
	private String firstname, lastname, username, email;
}
