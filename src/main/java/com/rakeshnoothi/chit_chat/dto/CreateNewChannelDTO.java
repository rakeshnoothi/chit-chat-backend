package com.rakeshnoothi.chit_chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateNewChannelDTO {
	private String name;
	private Long createdByUserId;
}
