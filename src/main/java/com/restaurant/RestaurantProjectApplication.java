package com.restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "com.restaurant.services.auth" })
public class RestaurantProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantProjectApplication.class, args);
	}

}
