package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.security.entities.User;
import com.jcanseco.inventoryapi.security.repositories.RoleRepository;
import com.jcanseco.inventoryapi.security.repositories.UserRepository;
import com.jcanseco.inventoryapi.security.specifications.UserSpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;
import java.time.LocalDateTime;
import java.time.Month;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @Sql("/multiple-users.sql")
    public void createUserShouldGenerateId() {

        var role = roleRepository.findById(1L)
                .orElseThrow();

        var user = userRepository.saveAndFlush(User.builder()
                .fullName("Smith Doe Jr")
                .email("smith.doe.jr@mail.com")
                .password("mockPassword")
                .role(role)
                .build());

        assertTrue(user.getId() > 0);
        assertNotNull(user.getCreatedAt());
    }

    @Test
    @Sql("/multiple-users.sql")
    public void createUserWhenEmailAlreadyExistsShouldThrowException() {
        var role = roleRepository.findById(1L)
                .orElseThrow();
        assertThrows(DataIntegrityViolationException.class, () -> userRepository.saveAndFlush(User.builder()
                .fullName("Smith Doe Jr")
                .email("john_doe@gmail.com")
                .password("mockPassword")
                .role(role)
                .build())
        );
    }

    @Test
    @Sql("/multiple-users.sql")
    public void getUsersByEmailShouldBeExpected() {
        var expectedEmail = "john_doe@gmail.com";
        var user = userRepository.findOne(
                UserSpecifications.byEmail("john_doe@gmail.com")
        ).orElseThrow();
        assertEquals(expectedEmail, user.getEmail());
    }

    @Test
    @Sql("/multiple-users.sql")
    public void getUsersByEmailLikeShouldReturnExpectedList() {
        var users = userRepository.findAll(
                UserSpecifications.byEmailLike("doe")
        );
        assertEquals(2, users.size());
    }

    @Test
    @Sql("/multiple-users.sql")
    public void getUsersByRoleShouldReturnExpectedList() {
        var role = roleRepository.findById(1L).orElseThrow();
        var users = userRepository.findAll(
                UserSpecifications.byRole(role)
        );
        assertEquals(2, users.size());
    }

    @Test
    @Sql("/multiple-users.sql")
    public void getUsersByFullNameLikeShouldReturnExpectedList() {
        var users = userRepository.findAll(
                UserSpecifications.byFullNameLike("jane")
        );
        assertEquals(2, users.size());
    }

    @Test
    @Sql("/multiple-users.sql")
    public void getUsersOrderByIdAscFirstUserIdShouldBeOne() {
        var expectedUserId = 1L;
        var users = userRepository.findAll(
                UserSpecifications.orderByIdAsc(Specification.where(null))
        );
        var firstUserId = users.get(0).getId();
        assertEquals(expectedUserId, firstUserId);
    }

    @Test
    @Sql("/multiple-users.sql")
    public void getUsersOrderByIdDescFirstUserIdShouldBeFour() {
        var expectedUserId = 4L;
        var users = userRepository.findAll(
                UserSpecifications.orderByIdDesc(Specification.where(null))
        );
        var firstUserId = users.get(0).getId();
        assertEquals(expectedUserId, firstUserId);
    }

    @Test
    @Sql("/multiple-users.sql")
    public void getUsersOrderByFullNameAscFirstUserNameShouldBeExpected() {
        var expectedFullName = "Jane Doe";
        var users = userRepository.findAll(
                UserSpecifications.orderByFullNameAsc(Specification.where(null))
        );
        var firstFullName = users.get(0).getFullName();
        assertEquals(expectedFullName, firstFullName);
    }

    @Test
    @Sql("/multiple-users.sql")
    public void getUsersOrderByFullNameDescFirstUserNameShouldBeExpected() {
        var expectedFullName = "John Smith";
        var users = userRepository.findAll(
                UserSpecifications.orderByFullNameDesc(Specification.where(null))
        );
        var firstFullName = users.get(0).getFullName();
        assertEquals(expectedFullName, firstFullName);
    }

    @Test
    @Sql("/multiple-users.sql")
    public void getUsersOrdersByRoleAscFirstRoleNameShouldBeExpected() {
        var expectedRoleName = "Categories";
        var users = userRepository.findAll(
                UserSpecifications.orderByRoleAsc(Specification.where(null))
        );
        var roleName = users.get(0).getRole().getName();
        assertEquals(expectedRoleName, roleName);
    }

    @Test
    @Sql("/multiple-users.sql")
    public void getUsersOrdersByRoleDescFirstRoleNameShouldBeExpected() {
        var expectedRoleName = "Dashboard";
        var users = userRepository.findAll(
                UserSpecifications.orderByRoleDesc(Specification.where(null))
        );
        var roleName = users.get(0).getRole().getName();
        assertEquals(expectedRoleName, roleName);
    }

    @Test
    @Sql("/multiple-users.sql")
    public void getUsersOrderByEmailAscFirstUserEmailShouldBeExpected() {
        var expectedEmail = "jane_doe@gmail.com";
        var users = userRepository.findAll(
                UserSpecifications.orderByEmailAsc(Specification.where(null))
        );
        var roleName = users.get(0).getEmail();
        assertEquals(expectedEmail, roleName);
    }

    @Test
    @Sql("/multiple-users.sql")
    public void getUsersOrderByEmailDescFirstUserEmailShouldBeExpected() {
        var expectedEmail = "john_smith@gmail.com";
        var users = userRepository.findAll(
                UserSpecifications.orderByEmailDesc(Specification.where(null))
        );
        var roleName = users.get(0).getEmail();
        assertEquals(expectedEmail, roleName);
    }

    @Test
    @Sql("/multiple-users.sql")
    public void getUsersOrderByCreatedAtAscFirstUserCreatedAtShouldBeExpected() {
        var expectedCreatedAt = LocalDateTime.of(2023, Month.MAY, 1, 0, 0);
        var users = userRepository.findAll(
                UserSpecifications.orderByCreatedAtAsc(Specification.where(null))
        );
        var createdAt = users.get(0).getCreatedAt();
        assertEquals(expectedCreatedAt, createdAt);
    }

    @Test
    @Sql("/multiple-users.sql")
    public void getUsersOrderByCreatedAtDescFirstUserCreatedAtShouldBeExpected() {
        var expectedCreatedAt = LocalDateTime.of(2023, Month.MAY, 22, 0, 0);
        var users = userRepository.findAll(
                UserSpecifications.orderByCreatedAtDesc(Specification.where(null))
        );
        var createdAt = users.get(0).getCreatedAt();
        assertEquals(expectedCreatedAt, createdAt);
    }

    @Test
    @Sql("/multiple-users.sql")
    public void getUsersOrderByUpdatedAtAscFirstUserCreatedAtShouldBeExpected() {
        var expectedUpdatedAt = LocalDateTime.of(2023, Month.MAY, 2, 0, 0);
        var users = userRepository.findAll(
                UserSpecifications.orderByUpdatedAtAsc(Specification.where(null))
        );
        var updatedAt = users.get(0).getUpdatedAt();
        assertEquals(expectedUpdatedAt, updatedAt);
    }

    @Test
    @Sql("/multiple-users.sql")
    public void getUsersOrderByUpdatedAtDescFirstUserCreatedAtShouldBeExpected() {
        var expectedUpdatedAt = LocalDateTime.of(2023, Month.MAY, 23, 0, 0);
        var users = userRepository.findAll(
                UserSpecifications.orderByUpdatedAtDesc(Specification.where(null))
        );
        var updatedAt = users.get(0).getUpdatedAt();
        assertEquals(expectedUpdatedAt, updatedAt);
    }
}
