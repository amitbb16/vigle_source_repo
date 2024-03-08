package com.retro.dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RetroApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetroApplication.class, args);		
	}

}