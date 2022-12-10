package com.dcagon.decapay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DecapayApplication {

	public static void main(String[] args) {
		SpringApplication.run(DecapayApplication.class, args);
	}

}
