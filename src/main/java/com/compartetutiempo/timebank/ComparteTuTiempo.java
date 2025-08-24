package com.compartetutiempo.timebank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.compartetutiempo")
public class ComparteTuTiempo {

	public static void main(String[] args) {
		SpringApplication.run(ComparteTuTiempo.class, args);
	}

}
