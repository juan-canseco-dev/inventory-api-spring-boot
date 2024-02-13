package com.jcanseco.inventoryapi.security.initializers;

import com.jcanseco.inventoryapi.security.constants.AuthConstants;
import com.jcanseco.inventoryapi.security.entities.Role;
import com.jcanseco.inventoryapi.security.entities.User;
import com.jcanseco.inventoryapi.security.repositories.RoleRepository;
import com.jcanseco.inventoryapi.security.repositories.UserRepository;
import com.jcanseco.inventoryapi.security.services.ResourceService;
import com.jcanseco.inventoryapi.security.specifications.RoleSpecifications;
import com.jcanseco.inventoryapi.security.specifications.UserSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AdminDataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResourceService resourceService;

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (userRepository.exists(UserSpecifications.byEmail(AuthConstants.ADMIN_EMAIL))) {
            return;
        }

        if (roleRepository.exists(RoleSpecifications.byName(AuthConstants.ADMIN_ROLE_NAME))) {
            return;
        }

        System.out.println("Creating Admin User & Admin Role");
        var role = roleRepository.saveAndFlush(
                Role.builder()
                        .name(AuthConstants.ADMIN_ROLE_NAME)
                        .permissions(resourceService.allPermissions())
                        .build()
        );

        userRepository.saveAndFlush(
                User.builder()
                        .fullName("System Admin")
                        .role(role)
                        .email(AuthConstants.ADMIN_EMAIL)
                        .password(passwordEncoder.encode(AuthConstants.ADMIN_PASSWORD))
                        .build()
        );

        System.out.println("Admin User & Admin Role Created.");
    }
}
