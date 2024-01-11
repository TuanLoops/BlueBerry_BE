package com.blueberry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BlueBerryApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlueBerryApplication.class, args);
	}

}
