package com.boot.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

	private String username;
	private String fullName;
	private String email;
	private String password;
	private String passwordConfirm;

}
