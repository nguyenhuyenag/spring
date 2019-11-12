package com.dto;

import java.util.List;

import com.entity.Role;

import lombok.Data;

@Data
public class UserData {

	private String username;
	private String password;
	private String email;
	List<Role> roles;

}