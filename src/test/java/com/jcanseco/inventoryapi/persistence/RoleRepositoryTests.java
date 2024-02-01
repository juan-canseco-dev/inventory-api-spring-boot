package com.jcanseco.inventoryapi.persistence;

import com.jcanseco.inventoryapi.security.entities.Role;
import com.jcanseco.inventoryapi.security.repositories.RoleRepository;
import static com.jcanseco.inventoryapi.security.specifications.RoleSpecifications.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository repository;

    @Test
    public void createRoleShouldGenerateId() {

        var permissions = new HashSet<>(
                List.of(
                        "Permissions.Dashboard.View",
                        "Permissions.Categories.View",
                        "Permissions.Products.View",
                        "Permissions.Products.Create",
                        "Permissions.Customers.View"
                )
        );

        var role = Role.builder()
                .name("New Role")
                .permissions(permissions)
                .build();

        var newRole = repository.saveAndFlush(role);
        assertTrue(newRole.getId() > 0);
        assertNotNull(newRole.getCreatedAt());
        assertNotNull(newRole.getPermissions());
    }

    @Sql("/multiple-roles.sql")
    @Test
    public void getRolesByNameLikeSpecificationShouldReturnList() {
        var roles = repository.findAll(
                byNameLike("p")
        );
        assertNotNull(roles);
        assertEquals(3, roles.size());
    }

    @Sql("/multiple-roles.sql")
    @Test
    public void getRolesOrderByIdAscFirstRoleIdShouldBeOne() {
        var roles = repository.findAll(
                orderByIdAsc(Specification.where(null))
        );
        assertNotNull(roles);
        assertEquals(7, roles.size());
        var firstRole = roles.get(0);
        assertEquals(1L, firstRole.getId());
    }

    @Sql("/multiple-roles.sql")
    @Test
    public void getRolesOrderByIdDescFirstRoleIdShouldBeSeven() {
        var roles = repository.findAll(
                orderByIdDesc(Specification.where(null))
        );
        assertNotNull(roles);
        assertEquals(7, roles.size());
        var firstRole = roles.get(0);
        assertEquals(7L, firstRole.getId());
    }

    @Sql("/multiple-roles.sql")
    @Test
    public void getRolesOrderByNameAscFirstRoleNameShouldBeExpected() {
        var roles = repository.findAll(
                orderByNameAsc(Specification.where(null))
        );
        assertNotNull(roles);
        assertEquals(7, roles.size());
        var firstRole = roles.get(0);
        assertEquals("Categories", firstRole.getName());
    }

    @Sql("/multiple-roles.sql")
    @Test
    public void getRolesOrderByNameDescFirstRoleNameShouldBeExpected() {
        var roles = repository.findAll(
                orderByNameDesc(Specification.where(null))
        );
        assertNotNull(roles);
        assertEquals(7, roles.size());
        var firstRole = roles.get(0);
        assertEquals("Units of Measurement", firstRole.getName());
    }

    @Sql("/multiple-roles.sql")
    @Test
    public void getRolesOrderByCreatedAtAscFirstRoleCreatedAtShouldBeExpected() {
        var expectedCreatedAt = LocalDateTime.of(2023, Month.MAY, 1, 0, 0);
        var roles = repository.findAll(
                orderByCreatedAtAsc(Specification.where(null))
        );
        assertNotNull(roles);
        assertEquals(7, roles.size());
        var firstRole = roles.get(0);
        assertEquals(expectedCreatedAt, firstRole.getCreatedAt());
    }

    @Sql("/multiple-roles.sql")
    @Test
    public void getRolesOrderByCreatedAtDescFirstRoleCreatedAtShouldBeExpected() {
        var expectedCreatedAt = LocalDateTime.of(2023, Month.JUNE, 12, 0, 0);
        var roles = repository.findAll(
                orderByCreatedAtDesc(Specification.where(null))
        );
        assertNotNull(roles);
        assertEquals(7, roles.size());
        var firstRole = roles.get(0);
        assertEquals(expectedCreatedAt, firstRole.getCreatedAt());
    }

    // Updated At

    @Sql("/multiple-roles.sql")
    @Test
    public void getRolesOrderByUpdatedAtAscFirstRoleUpdatedAtShouldBeExpected() {
        var expectedCreatedAt = LocalDateTime.of(2023, Month.MAY, 2, 0, 0);
        var roles = repository.findAll(
                orderByUpdatedAtAsc(Specification.where(null))
        );
        assertNotNull(roles);
        assertEquals(7, roles.size());
        var firstRole = roles.get(0);
        assertEquals(expectedCreatedAt, firstRole.getUpdatedAt());
    }

    @Sql("/multiple-roles.sql")
    @Test
    public void getRolesOrderByUpdatedAtDescFirstRoleUpdatedAtShouldBeExpected() {
        var expectedCreatedAt = LocalDateTime.of(2023, Month.JUNE, 13, 0, 0);
        var roles = repository.findAll(
                orderByUpdatedAtDesc(Specification.where(null))
        );
        assertNotNull(roles);
        assertEquals(7, roles.size());
        var firstRole = roles.get(0);
        assertEquals(expectedCreatedAt, firstRole.getUpdatedAt());
    }
}
