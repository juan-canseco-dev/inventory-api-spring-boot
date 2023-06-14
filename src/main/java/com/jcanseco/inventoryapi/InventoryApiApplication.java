package com.jcanseco.inventoryapi;

import com.jcanseco.inventoryapi.entities.Category;
import com.jcanseco.inventoryapi.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.util.List;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class InventoryApiApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(InventoryApiApplication.class, args);
	}
	@Autowired
	private CategoryRepository repository;
	@Override
	public void run(String... args) throws Exception {
		var categories = List.of(
				Category.builder().name("Electronics").build(),
				Category.builder().name("Video").build(),
				Category.builder().name("Video Games").build()
		);

		repository.saveAllAndFlush(categories);
	}
}
