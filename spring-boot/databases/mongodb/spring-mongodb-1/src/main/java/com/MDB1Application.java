package com;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.repository.RestaurantRepository;

// @EnableMongoAuditing
// @EnableMongoRepositories
@SpringBootApplication
public class MDB1Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MDB1Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }

}