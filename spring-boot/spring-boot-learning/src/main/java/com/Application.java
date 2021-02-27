package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.repository.UserRepository;
import com.service.SpringTransaction;
import com.service.UserService;

@SpringBootApplication
@EnableAutoConfiguration
public class Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	UserService service;

	@Autowired
	SpringTransaction iTest;

	@Autowired
	UserRepository userRepository;

	@Override
	public void run(String... args) throws Exception {
		// PageableJPA.init();
		// PageableJPA.info();
		// PageableJPA.sortPage();
		// PageableJPA.showAllPage();
		// userRepository.getAllEmails().forEach(t->System.out.println(t));
		// System.out.println(userRepository.getAllEmails());
	}

}