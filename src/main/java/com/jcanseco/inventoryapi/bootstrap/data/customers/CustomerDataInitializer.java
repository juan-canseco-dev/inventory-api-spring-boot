package com.jcanseco.inventoryapi.bootstrap.data.customers;

import com.jcanseco.inventoryapi.customers.domain.Customer;
import com.jcanseco.inventoryapi.customers.persistence.CustomerRepository;
import com.jcanseco.inventoryapi.shared.address.Address;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;






@Profile("!test")
@Slf4j
@Order(8)
@Component
@RequiredArgsConstructor
public class CustomerDataInitializer implements ApplicationRunner {

    private static final int NUMBER_OF_CUSTOMERS_TO_INITIALIZE = 40;

    private final CustomerRepository customerRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Starting customer data initialization.");

        if (customerRepository.count() > 0) {
            log.info("Skipping customer initialization because customers already exist.");
            return;
        }

        Faker faker = new Faker(new Locale("es", "MX"));
        List<Customer> customers = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_CUSTOMERS_TO_INITIALIZE; i++) {

            String dni = faker.idNumber().valid();
            String phone = faker.phoneNumber().cellPhoneInternational();
            String fullName = faker.name().fullName();

            boolean exists = customers.stream().anyMatch(customer ->
                    customer.getDni().equalsIgnoreCase(dni) ||
                            customer.getPhone().equalsIgnoreCase(phone) ||
                            customer.getFullName().equalsIgnoreCase(fullName)
            );

            if (exists) {
                log.warn("Skipping duplicated customer. dni={}, phone={}, fullName={}", dni, phone, fullName);
                continue;
            }

            var customerAddress = Address.builder()
                    .country(faker.address().country())
                    .state(faker.address().state())
                    .city(faker.address().city())
                    .street(faker.address().streetAddress())
                    .zipCode(faker.address().zipCode())
                    .build();

            var customer = Customer.builder()
                    .dni(dni)
                    .phone(phone)
                    .fullName(fullName)
                    .address(customerAddress)
                    .build();

            customers.add(customer);

            log.info("Prepared customer: dni={}, phone={}, fullName={}", dni, phone, fullName);
        }

        customerRepository.saveAll(customers);

        log.info("Customer data initialization completed. Inserted {} customers.", customers.size());
    }
}





