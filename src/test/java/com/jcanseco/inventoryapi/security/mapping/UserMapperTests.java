package com.jcanseco.inventoryapi.security.mapping;

import com.jcanseco.inventoryapi.security.dtos.roles.RoleDetailsDto;
import com.jcanseco.inventoryapi.security.entities.Role;
import com.jcanseco.inventoryapi.security.entities.User;
import com.jcanseco.inventoryapi.security.mappers.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTests {
    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    private Role role;
    private User user;

    @BeforeEach
    public void setup() {

        role = Role.builder()
                .id(1L)
                .name("New Name")
                .permissions(new HashSet<>(List.of("Permissions.Dashboard.View")))
                .createdAt(LocalDateTime.now().plusMonths(2))
                .updatedAt(LocalDateTime.now().plusMonths(3))
                .build();

        user = User.builder()
                .id(1L)
                .role(role)
                .email("john_doe@mail.com")
                .fullName("John Doe")
                .password("JohnDoe1234")
                .createdAt(LocalDateTime.now().plusMonths(4))
                .updatedAt(LocalDateTime.now().plusMonths(5))
                .build();
    }

    @Test
    public void entityToDto() {
        var dto = mapper.entityToDto(user);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getRole().getName(), dto.getRole());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getFullName(), dto.getFullName());
        assertEquals(user.getCreatedAt(), dto.getCreatedAt());
        assertEquals(user.getUpdatedAt(), dto.getUpdatedAt());
    }

    @Test
    public void entityToDetailsDto() {
        var dto = mapper.entityToDetailsDto(user);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getFullName(), dto.getFullName());
        assertEqualsRole(user.getRole(), dto.getRole());
        assertEquals(user.getCreatedAt(), dto.getCreatedAt());
        assertEquals(user.getUpdatedAt(), dto.getUpdatedAt());
    }

    @Test
    public void pageToPagedList() {

        var pageNumber = 1;
        var totalElementsInDb = 4;
        var totalPages = 2;
        var pageSize = 2;

        var users = List.of(
                User.builder()
                        .id(1L)
                        .fullName("John Doe")
                        .email("john.doe@mail.com")
                        .role(Role.builder().name("Admin").build())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now().plusDays(2))
                        .build(),

                User.builder()
                        .id(2L)
                        .fullName("Jane Doe")
                        .email("jane.doe@mail.com")
                        .role(Role.builder().name("Secretary").build())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now().plusDays(2))
                        .build()
        );

        var userDtos = users.stream().map(mapper::entityToDto).toList();

        Page<User> page = new PageImpl<>(users, Pageable.ofSize(pageSize), totalElementsInDb);
        var pagedList = mapper.pageToPagedList(page);

        assertNotNull(pagedList);
        assertEquals(pageNumber, pagedList.getPageNumber());
        assertEquals(pageSize, pagedList.getPageSize());
        assertEquals(totalElementsInDb, pagedList.getTotalElements());
        assertEquals(totalPages, pagedList.getTotalPages());
        assertFalse(pagedList.hasPreviousPage());
        assertTrue(pagedList.hasNextPage());
        assertThat(pagedList.getItems()).hasSameElementsAs(userDtos);
    }

    private void assertEqualsRole(Role expected, RoleDetailsDto actual) {
        assertEquals(role.getId(), actual.getId());
        assertEquals(role.getName(), actual.getName());
        assertThat(expected.getPermissions()).hasSameElementsAs(actual.getPermissions());
        assertEquals(role.getCreatedAt(), actual.getCreatedAt());
        assertEquals(role.getUpdatedAt(), actual.getUpdatedAt());
    }
}
