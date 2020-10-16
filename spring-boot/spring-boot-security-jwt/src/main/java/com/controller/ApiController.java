package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.dto.JSONClass;
import com.entity.User;
import com.repository.UserRepository;
import com.response.ApiResponse;
import com.util.DateTimeUtils;
import com.util.JsonUtils;

@RestController
@RequestMapping("api")
public class ApiController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private UserRepository repository;

	private static final String URL = "https://jsonplaceholder.typicode.com/todos";

	@GetMapping("public/timestamp")
	private ResponseEntity<ApiResponse> now() {
		String time = DateTimeUtils.getNow();
		ApiResponse api = new ApiResponse("OK_200", "Xử lý dữ liệu thành công", time);
		return new ResponseEntity<>(api, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('USER')")
	@GetMapping("get-json")
	private ResponseEntity<ApiResponse> getJson() {
		String json = restTemplate.getForObject(URL, String.class);
		List<JSONClass> list = JsonUtils.toList(json);
		ApiResponse api = new ApiResponse("OK_200", "Xử lý dữ liệu thành công", list);
		return new ResponseEntity<>(api, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("get-users")
	private ResponseEntity<List<User>> getUsers() {
		List<User> list = repository.findAll();
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

}
