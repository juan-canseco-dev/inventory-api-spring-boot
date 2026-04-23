package com.jcanseco.inventoryapi.bootstrap.data.catalog;

import com.jcanseco.inventoryapi.catalog.units.domain.UnitOfMeasurement;
import com.jcanseco.inventoryapi.catalog.units.persistence.UnitOfMeasurementRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;






@Profile("!test")
@Order(5)
@Slf4j
@Component
@RequiredArgsConstructor
public class UnitOfMeasurementDataInitializer implements ApplicationRunner {

    private final UnitOfMeasurementRepository repository;

    private static final List<String> DEFAULT_UNITS = List.of(
            "Piece",
            "Kilogram",
            "Gram",
            "Liter",
            "Milliliter",
            "Meter",
            "Centimeter",
            "Box",
            "Package",
            "Dozen",
            "Unit",
            "Bag"
    );

    @Transactional
    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting unit of measurement data initialization.");

        List<String> existingNames = repository.findAll().stream()
                .map(UnitOfMeasurement::getName)
                .toList();

        List<UnitOfMeasurement> unitsToInsert = DEFAULT_UNITS.stream()
                .filter(name -> !existingNames.contains(name))
                .map(name -> UnitOfMeasurement.builder()
                        .name(name)
                        .build())
                .toList();

        if (unitsToInsert.isEmpty()) {
            log.info("Skipping unit of measurement seeding. All default units already exist.");
            return;
        }

        log.info("Saving {} new units of measurement: {}",
                unitsToInsert.size(),
                unitsToInsert.stream().map(UnitOfMeasurement::getName).toList());

        repository.saveAll(unitsToInsert);

        log.info("Unit of measurement seeding completed successfully.");
    }
}





