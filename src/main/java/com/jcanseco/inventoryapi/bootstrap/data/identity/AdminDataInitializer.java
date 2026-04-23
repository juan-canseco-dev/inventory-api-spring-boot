package com.jcanseco.inventoryapi.bootstrap.data.identity;

import com.jcanseco.inventoryapi.identity.auth.security.AuthConstants;
import com.jcanseco.inventoryapi.identity.permissions.domain.PermissionCatalog;
import com.jcanseco.inventoryapi.identity.roles.domain.Role;
import com.jcanseco.inventoryapi.identity.roles.persistence.RoleRepository;
import com.jcanseco.inventoryapi.identity.roles.persistence.RoleSpecifications;
import com.jcanseco.inventoryapi.identity.users.domain.User;
import com.jcanseco.inventoryapi.identity.users.persistence.UserRepository;
import com.jcanseco.inventoryapi.identity.users.persistence.UserSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;





@Profile("!test")
@Order(1)
@Component
@RequiredArgsConstructor
public class AdminDataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionCatalog permissionCatalog;

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
                        .permissions(permissionCatalog.allPermissions())
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






