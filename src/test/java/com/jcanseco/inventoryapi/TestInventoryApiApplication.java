package com.jcanseco.inventoryapi;

import com.jcanseco.inventoryapi.shared.testing.TestcontainersConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
class TestInventoryApiApplication {
	public static void main(String[] args) {
		SpringApplication.from(InventoryApiApplication::main)
				.with(TestcontainersConfiguration.class)
				.run(args);
	}
}





