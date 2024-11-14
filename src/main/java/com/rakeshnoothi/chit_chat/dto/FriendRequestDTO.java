package com.rakeshnoothi.chit_chat.dto;

import java.time.LocalDateTime;

import com.rakeshnoothi.chit_chat.enums.RequestStatus;

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
public class FriendRequestDTO {
	private Long id;
    private LocalDateTime createdAt;
    private RequestStatus status;
    private UserDTO user;
}

