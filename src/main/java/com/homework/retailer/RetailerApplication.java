package com.homework.retailer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RetailerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetailerApplication.class, args);
	}
}
