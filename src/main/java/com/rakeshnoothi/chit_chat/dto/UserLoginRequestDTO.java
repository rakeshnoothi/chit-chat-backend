package com.rakeshnoothi.chit_chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class UserLoginRequestDTO {
	@NotBlank(message = "Username is mandatory")
    @Size(min = 5, max = 15, message = "Username must be between 5 and 15 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "Username can only contain letters, numbers, and underscores")
    private String username;
	
	@NotBlank(message = "password is mandatory")
	@Size(min = 8, max = 50, message = "password must be between 8 and 50 characters")
	private String password;
}
