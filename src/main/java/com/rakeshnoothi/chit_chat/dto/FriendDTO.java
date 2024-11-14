package com.rakeshnoothi.chit_chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FriendDTO {
	private Long id;
	private String firstname;
	private String lastname;
	private String username;
}
