package core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import core.repository.ClazzRepository;
import core.repository.StudentRepository;
import core.repository.UserRepository;

@SpringBootApplication
public class Application extends SpringBootServletInitializer implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	@Autowired
	ClazzRepository clazzRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	StudentRepository studentRepository;

	@Override
	public void run(String... params) throws Exception {

		// int code = RandomUtils.nextInt(0, 999);
		// String name = RandomStringUtils.randomAlphabetic(5);
		// userRepository.save(new User(code, name));

		// System.out.println(studentRepository.getByNativeQuery(2, 56));
		// System.out.println(studentRepository.getByNativeQuery(2, 56));

		// System.out.println(userRepository.getByNativeQuery(2, 5));
		System.out.println(userRepository.getByJpaQuery(2, 56));

	}

}