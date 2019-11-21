package com.controller;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dto.LoginRequest;
import com.dto.UserData;
import com.dto.UserResponse;
import com.entity.User;
import com.service.UserService;

@RestController
@RequestMapping("api/user")
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping("login")
	// public String login(@RequestParam String username, @RequestParam String password) {
	public String login(@RequestBody LoginRequest login) {
		return userService.signin(login.getUsername(), login.getPassword());
	}

	@PostMapping("/signup")
//	@ApiResponses(value = { //
//		@ApiResponse(code = 400, message = "Something went wrong"), //
//		@ApiResponse(code = 403, message = "Access denied"), //
//		@ApiResponse(code = 422, message = "Username is already in use"), //
//		@ApiResponse(code = 500, message = "Expired or invalid JWT token") })
	public String signup(@RequestBody UserData user) {
		return userService.signup(modelMapper.map(user, User.class));
	}

	@DeleteMapping(value = "/{username}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
//	@ApiResponses(value = { //
//		@ApiResponse(code = 400, message = "Something went wrong"), //
//		@ApiResponse(code = 403, message = "Access denied"), //
//		@ApiResponse(code = 404, message = "The user doesn't exist"), //
//		@ApiResponse(code = 500, message = "Expired or invalid JWT token") })
	public String delete(@PathVariable String username) {
		userService.delete(username);
		return username;
	}

	@GetMapping(value = "/{username}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
//	@ApiResponses(value = { //
//		@ApiResponse(code = 400, message = "Something went wrong"), //
//		@ApiResponse(code = 403, message = "Access denied"), //
//		@ApiResponse(code = 404, message = "The user doesn't exist"), //
//		@ApiResponse(code = 500, message = "Expired or invalid JWT token") })
	public UserResponse search(@PathVariable String username) {
		return modelMapper.map(userService.search(username), UserResponse.class);
	}

	@GetMapping(value = "/me")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
//	@ApiResponses(value = { //
//		@ApiResponse(code = 400, message = "Something went wrong"), //
//		@ApiResponse(code = 403, message = "Access denied"), //
//		@ApiResponse(code = 500, message = "Expired or invalid JWT token") })
	public UserResponse whoami(HttpServletRequest req) {
		return modelMapper.map(userService.whoami(req), UserResponse.class);
	}

	@GetMapping("/refresh")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	public String refresh(HttpServletRequest req) {
		return userService.refresh(req.getRemoteUser());
	}

}
