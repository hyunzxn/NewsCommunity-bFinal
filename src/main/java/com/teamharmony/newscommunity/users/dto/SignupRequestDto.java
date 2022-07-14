package com.teamharmony.newscommunity.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignupRequestDto {
	@NotBlank
	@Size(min = 2, max = 10)
	private String username;
	@NotBlank
	@Size(min = 8, max = 20)
	private String password;
}
