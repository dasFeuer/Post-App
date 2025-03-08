package com.example.barun;

import com.example.barun.enitities.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing
public class BarunApplication {

	public static void main(String[] args) {
		SpringApplication.run(BarunApplication.class, args);
	}
}
