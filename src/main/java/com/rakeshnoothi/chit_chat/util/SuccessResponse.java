package com.rakeshnoothi.chit_chat.util;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class SuccessResponse {
	@Builder.Default
	private LocalDateTime timestamp = LocalDateTime.now();
	private Integer statusCode;
	private String message;
	private Object body;
}
