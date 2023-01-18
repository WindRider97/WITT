package ch.hearc.jee2022.witt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class WittProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(WittProjectApplication.class, args);
	}

}
