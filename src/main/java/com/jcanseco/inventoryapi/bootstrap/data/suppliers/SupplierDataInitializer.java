package com.jcanseco.inventoryapi.bootstrap.data.suppliers;

import com.jcanseco.inventoryapi.shared.address.Address;
import com.jcanseco.inventoryapi.suppliers.domain.Supplier;
import com.jcanseco.inventoryapi.suppliers.persistence.SupplierRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;





@Profile("!test")
@Order(6)
@Slf4j
@Component
@RequiredArgsConstructor
public class SupplierDataInitializer implements ApplicationRunner {

    private static final int NUMBER_OF_SUPPLIERS = 20;

    private final SupplierRepository supplierRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (supplierRepository.count() > 0) {
            log.info("Skipping supplier initialization because suppliers already exist.");
            return;
        }

        Faker faker = new Faker();
        List<Supplier> suppliers = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_SUPPLIERS; i++) {
            String companyName = faker.company().name();
            String contactPhone = faker.phoneNumber().phoneNumberInternational();

            boolean exists = suppliers.stream().anyMatch(supplier ->
                    supplier.getCompanyName().equalsIgnoreCase(companyName)
                            || supplier.getContactPhone().equals(contactPhone)
            );

            if (exists) {
                continue;
            }

            var supplierAddress = Address.builder()
                    .country(faker.address().country())
                    .state(faker.address().state())
                    .city(faker.address().city())
                    .street(faker.address().streetAddress())
                    .zipCode(faker.address().zipCode())
                    .build();

            var newSupplier = Supplier.builder()
                    .companyName(companyName)
                    .contactName(faker.name().fullName())
                    .contactPhone(contactPhone)
                    .address(supplierAddress)
                    .build();

            suppliers.add(newSupplier);
        }

        supplierRepository.saveAll(suppliers);
        log.info("Inserted {} suppliers.", suppliers.size());
    }
}





