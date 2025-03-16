package com.example.barun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BarunApplication {

	public static void main(String[] args) {
		SpringApplication.run(BarunApplication.class, args);
	}
}
