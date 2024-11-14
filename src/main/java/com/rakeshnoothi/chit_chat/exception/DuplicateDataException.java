package com.rakeshnoothi.chit_chat.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DuplicateDataException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public DuplicateDataException(String message) {
		super(message);
	}
}
