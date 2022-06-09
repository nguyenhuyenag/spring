package com.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("api")
public class JokesController {

	// @Autowired
	// private UserRepository repository;

	private static final String URL = "https://jsonplaceholder.typicode.com/todos";

	@GetMapping("get-json")
	private ResponseEntity<String> getJson() {
		RestTemplate restTemplate = new RestTemplate();
		String json = "";
		try {
			json = restTemplate.getForObject(URL, String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(json, HttpStatus.OK);
	}

//	@GetMapping("user-info")
//	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
//	// @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//	public ResponseEntity<User> userInfo(Principal principal) {
//		String username = principal.getName();
//		Optional<User> opt = repository.findByUsername(username);
//		User user = opt.get();
//		user.setPassword(null);
//		user.setRoles(null);
//		return new ResponseEntity<>(user, HttpStatus.OK);
//	}
//
//	@GetMapping("get-all-user")
//	@PreAuthorize("hasRole('ADMIN')")
//	public List<User> listUser() {
//		return repository.findAll();
//	}

}
