package ru.pssbd.fonds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class FondsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FondsApplication.class, args);
	}

}
