package com.jcanseco.inventoryapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.util.List;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class InventoryApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(InventoryApiApplication.class, args);
	}
}
