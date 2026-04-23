package com.jcanseco.inventoryapi.bootstrap.data.identity;

import com.jcanseco.inventoryapi.identity.roles.domain.Role;
import com.jcanseco.inventoryapi.identity.roles.persistence.RoleRepository;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import static com.jcanseco.inventoryapi.identity.permissions.domain.ActionType.*;
import static com.jcanseco.inventoryapi.identity.permissions.domain.Permissions.permissionOf;
import static com.jcanseco.inventoryapi.identity.permissions.domain.ResourceType.*;






@Profile("!test")
@Order(2)
@Slf4j
@Component
@RequiredArgsConstructor
public class RolesDataInitializer implements ApplicationRunner {

    private final RoleRepository repository;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting role data initialization.");

        long existingRoles = repository.count();
        if (existingRoles > 1) {
            log.info("Skipping role seeding. Found {} existing roles.", existingRoles);
            return;
        }

        List<Role> roles = List.of(
                buildRole("Inventory Manager", Set.of(
                        permissionOf(Dashboard, View),
                        permissionOf(UnitsOfMeasurement, View),
                        permissionOf(UnitsOfMeasurement, Create),
                        permissionOf(UnitsOfMeasurement, Update),
                        permissionOf(Categories, View),
                        permissionOf(Categories, Create),
                        permissionOf(Categories, Update),
                        permissionOf(Suppliers, View),
                        permissionOf(Products, View),
                        permissionOf(Products, Create),
                        permissionOf(Products, Update),
                        permissionOf(Products, Delete),
                        permissionOf(Purchases, View),
                        permissionOf(Purchases, Create),
                        permissionOf(Purchases, Update),
                        permissionOf(Purchases, Receive),
                        permissionOf(Orders, View)
                )),

                buildRole("Warehouse Manager", Set.of(
                        permissionOf(Dashboard, View),
                        permissionOf(Products, View),
                        permissionOf(Purchases, View),
                        permissionOf(Purchases, Receive),
                        permissionOf(Orders, View),
                        permissionOf(Orders, Receive),
                        permissionOf(Categories, View),
                        permissionOf(UnitsOfMeasurement, View)
                )),

                buildRole("Warehouse Clerk", Set.of(
                        permissionOf(Dashboard, View),
                        permissionOf(Products, View),
                        permissionOf(Purchases, View),
                        permissionOf(Purchases, Receive),
                        permissionOf(Orders, View),
                        permissionOf(Orders, Receive)
                )),

                buildRole("Purchasing Manager", Set.of(
                        permissionOf(Dashboard, View),
                        permissionOf(Suppliers, View),
                        permissionOf(Suppliers, Create),
                        permissionOf(Suppliers, Update),
                        permissionOf(Products, View),
                        permissionOf(Purchases, View),
                        permissionOf(Purchases, Create),
                        permissionOf(Purchases, Update),
                        permissionOf(Purchases, Receive)
                )),

                buildRole("Purchasing Assistant", Set.of(
                        permissionOf(Dashboard, View),
                        permissionOf(Suppliers, View),
                        permissionOf(Products, View),
                        permissionOf(Purchases, View),
                        permissionOf(Purchases, Create),
                        permissionOf(Purchases, Update)
                )),

                buildRole("Sales Manager", Set.of(
                        permissionOf(Dashboard, View),
                        permissionOf(Customers, View),
                        permissionOf(Customers, Create),
                        permissionOf(Customers, Update),
                        permissionOf(Customers, Delete),
                        permissionOf(Products, View),
                        permissionOf(Orders, View),
                        permissionOf(Orders, Create),
                        permissionOf(Orders, Update),
                        permissionOf(Orders, Delete),
                        permissionOf(Orders, Receive)
                )),

                buildRole("Sales Representative", Set.of(
                        permissionOf(Dashboard, View),
                        permissionOf(Customers, View),
                        permissionOf(Customers, Create),
                        permissionOf(Customers, Update),
                        permissionOf(Products, View),
                        permissionOf(Orders, View),
                        permissionOf(Orders, Create),
                        permissionOf(Orders, Update)
                )),

                buildRole("Customer Service", Set.of(
                        permissionOf(Dashboard, View),
                        permissionOf(Customers, View),
                        permissionOf(Customers, Create),
                        permissionOf(Customers, Update),
                        permissionOf(Orders, View),
                        permissionOf(Orders, Update),
                        permissionOf(Products, View)
                )),

                buildRole("Store Supervisor", Set.of(
                        permissionOf(Dashboard, View),
                        permissionOf(Products, View),
                        permissionOf(Customers, View),
                        permissionOf(Orders, View),
                        permissionOf(Orders, Create),
                        permissionOf(Orders, Update),
                        permissionOf(Orders, Receive),
                        permissionOf(Purchases, View)
                )),

                buildRole("Stock Controller", Set.of(
                        permissionOf(Dashboard, View),
                        permissionOf(Products, View),
                        permissionOf(Products, Update),
                        permissionOf(Purchases, View),
                        permissionOf(Purchases, Receive),
                        permissionOf(Orders, View),
                        permissionOf(Orders, Receive)
                )),

                buildRole("Catalog Manager", Set.of(
                        permissionOf(Dashboard, View),
                        permissionOf(UnitsOfMeasurement, View),
                        permissionOf(Categories, View),
                        permissionOf(Categories, Create),
                        permissionOf(Categories, Update),
                        permissionOf(Products, View),
                        permissionOf(Products, Create),
                        permissionOf(Products, Update)
                )),

                buildRole("Product Manager", Set.of(
                        permissionOf(Dashboard, View),
                        permissionOf(Categories, View),
                        permissionOf(UnitsOfMeasurement, View),
                        permissionOf(Suppliers, View),
                        permissionOf(Products, View),
                        permissionOf(Products, Create),
                        permissionOf(Products, Update),
                        permissionOf(Products, Delete)
                )),

                buildRole("Supplier Manager", Set.of(
                        permissionOf(Dashboard, View),
                        permissionOf(Suppliers, View),
                        permissionOf(Suppliers, Create),
                        permissionOf(Suppliers, Update),
                        permissionOf(Suppliers, Delete),
                        permissionOf(Purchases, View),
                        permissionOf(Purchases, Create),
                        permissionOf(Purchases, Update)
                )),

                buildRole("Customer Manager", Set.of(
                        permissionOf(Dashboard, View),
                        permissionOf(Customers, View),
                        permissionOf(Customers, Create),
                        permissionOf(Customers, Update),
                        permissionOf(Customers, Delete),
                        permissionOf(Orders, View)
                )),

                buildRole("Operations Analyst", Set.of(
                        permissionOf(Dashboard, View),
                        permissionOf(Products, View),
                        permissionOf(Purchases, View),
                        permissionOf(Orders, View),
                        permissionOf(Suppliers, View),
                        permissionOf(Customers, View),
                        permissionOf(Categories, View),
                        permissionOf(UnitsOfMeasurement, View)
                )),

                buildRole("Auditor", Set.of(
                        permissionOf(Dashboard, View),
                        permissionOf(Users, View),
                        permissionOf(Users, Export),
                        permissionOf(Roles, View),
                        permissionOf(Products, View),
                        permissionOf(Purchases, View),
                        permissionOf(Orders, View),
                        permissionOf(Suppliers, View),
                        permissionOf(Customers, View)
                )),

                buildRole("Reporting Analyst", Set.of(
                        permissionOf(Dashboard, View),
                        permissionOf(Users, View),
                        permissionOf(Users, Export),
                        permissionOf(Products, View),
                        permissionOf(Purchases, View),
                        permissionOf(Orders, View),
                        permissionOf(Customers, View),
                        permissionOf(Suppliers, View)
                )),

                buildRole("Security Administrator", Set.of(
                        permissionOf(Dashboard, View),
                        permissionOf(Users, View),
                        permissionOf(Users, Create),
                        permissionOf(Users, Update),
                        permissionOf(Users, Delete),
                        permissionOf(Users, ChangeRole),
                        permissionOf(Roles, View),
                        permissionOf(Roles, Create),
                        permissionOf(Roles, Update),
                        permissionOf(Roles, Delete)
                )),

                buildRole("Read Only User", Set.of(
                        permissionOf(Dashboard, View),
                        permissionOf(Users, View),
                        permissionOf(Roles, View),
                        permissionOf(UnitsOfMeasurement, View),
                        permissionOf(Categories, View),
                        permissionOf(Suppliers, View),
                        permissionOf(Customers, View),
                        permissionOf(Products, View),
                        permissionOf(Purchases, View),
                        permissionOf(Orders, View)
                ))
        );


        repository.saveAll(roles);

        log.info("Role seeding completed successfully. Inserted {} roles.", roles.size());
        log.info("Generated roles: {}", roles.stream().map(Role::getName).toList());
    }

    private Role buildRole(String roleName, Set<String> permissions) {
        return Role.builder()
                .name(roleName)
                .permissions(permissions)
                .build();
    }

}





