package com.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.primary.repository.MyTransactionService;

@Component
public class AppRunner implements CommandLineRunner {

	@Autowired
	// JdbcTemplateService service;
	// EntityManagerService service;
	// RepositoryService service;
	MyTransactionService service;

	@Override
	public void run(String... args) throws Exception {
		// service.save();
		// service.saveAll();
		// service.findAll();
		// service.showDataSourceURL();
		service.testTransaction();
	}

}
