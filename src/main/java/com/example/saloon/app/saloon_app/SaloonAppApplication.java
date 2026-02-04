package com.example.saloon.app.saloon_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Service;

@EnableJpaAuditing
@SpringBootApplication
public class  SaloonAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaloonAppApplication.class, args);
	}

}
