package com.rakeshnoothi.chit_chat.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String message) {
        super(message);
    }
}
