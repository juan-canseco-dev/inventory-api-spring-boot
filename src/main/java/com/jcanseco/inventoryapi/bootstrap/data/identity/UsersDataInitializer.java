package com.jcanseco.inventoryapi.bootstrap.data.identity;

import com.jcanseco.inventoryapi.identity.auth.security.AuthConstants;
import com.jcanseco.inventoryapi.identity.roles.domain.Role;
import com.jcanseco.inventoryapi.identity.roles.persistence.RoleRepository;
import com.jcanseco.inventoryapi.identity.users.domain.User;
import com.jcanseco.inventoryapi.identity.users.persistence.UserRepository;
import jakarta.transaction.Transactional;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;





@Profile("!test")
@Order(3)
@Slf4j
@Component
@RequiredArgsConstructor
public class UsersDataInitializer implements ApplicationRunner {

    private static final int USERS_PER_ROLE = 2;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting users data initialization.");

        long existingUsers = userRepository.count();
        if (existingUsers > 1) {
            log.info("Skipping user seeding. Found {} existing users.", existingUsers);
            return;
        }

        List<Role> roles = roleRepository.findAll();
        if (roles.isEmpty()) {
            log.warn("Skipping user seeding. No roles found.");
            return;
        }

        Faker faker = new Faker();
        List<User> usersToSave = new ArrayList<>();

        for (Role role : roles) {
            for (int i = 0; i < USERS_PER_ROLE; i++) {
                String fullName = faker.name().fullName();
                String email = buildEmailFromFullName(fullName);

                if (userRepository.findByFullName(fullName).isPresent()) {
                    log.info("Skipping user for role {}. Full name already exists: {}", role.getName(), fullName);
                    continue;
                }

                if (userRepository.findByEmail(email).isPresent()) {
                    log.info("Skipping user for role {}. Email already exists: {}", role.getName(), email);
                    continue;
                }

                User newUser = User.builder()
                        .fullName(fullName)
                        .role(role)
                        .email(email)
                        .password(passwordEncoder.encode(AuthConstants.DEFAULT_PASSWORD))
                        .build();

                usersToSave.add(newUser);
            }
        }

        if (usersToSave.isEmpty()) {
            log.info("No new users to insert.");
            return;
        }

        log.info("Generated users: {}",
                usersToSave.stream()
                        .map(user -> user.getFullName() + " <" + user.getEmail() + ">")
                        .toList());

        userRepository.saveAll(usersToSave);

        log.info("User seeding completed successfully. Inserted {} users.", usersToSave.size());
    }

    private String buildEmailFromFullName(String fullName) {
        String normalized = Normalizer.normalize(fullName, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        String localPart = normalized
                .toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9]+", ".")
                .replaceAll("\\.{2,}", ".")
                .replaceAll("(^\\.|\\.$)", "");

        return localPart + "@inventory.local";
    }
}





