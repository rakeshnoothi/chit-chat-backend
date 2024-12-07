package com.rakeshnoothi.chit_chat.dto;

import java.time.LocalDateTime;

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
public class ChannelDTO {
	private Long id;
	private String name;
	private LocalDateTime createdAt;
	private Integer totalMembers;
}
