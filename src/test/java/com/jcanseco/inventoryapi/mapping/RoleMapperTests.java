package com.jcanseco.inventoryapi.mapping;

import com.jcanseco.inventoryapi.mappers.RoleMapper;
import com.jcanseco.inventoryapi.security.entities.Role;
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

public class RoleMapperTests {
    private final RoleMapper mapper = Mappers.getMapper(RoleMapper.class);

    private Role role;

    @BeforeEach
    public void setup() {
        role = Role.builder()
                .id(1L)
                .name("New Role")
                .permissions(new HashSet<>(List.of(
                        "Permissions.Categories.View",
                        "Permissions.Products.View"
                )))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now().plusDays(20))
                .build();
    }

    @Test
    public void entityToDto() {
        var dto = mapper.entityToDto(role);
        assertEquals(role.getId(), dto.getId());
        assertEquals(role.getName(), dto.getName());
        assertEquals(role.getUpdatedAt(), dto.getUpdatedAt());
        assertEquals(role.getCreatedAt(), dto.getCreatedAt());
    }

    @Test
    public void entityToDetailsDto() {
        var dto = mapper.entityToDetailsDto(role);
        assertEquals(role.getId(), dto.getId());
        assertEquals(role.getName(), dto.getName());
        assertEquals(role.getUpdatedAt(), dto.getUpdatedAt());
        assertEquals(role.getCreatedAt(), dto.getCreatedAt());
        assertEquals(role.getPermissions(), new HashSet<>(dto.getPermissions()));
    }

    @Test
    public void pageToPagedList() {

        var pageNumber = 1;
        var totalElementsInDb = 4;
        var totalPages = 2;
        var pageSize = 2;

        var roles = List.of(
                Role.builder()
                        .id(1L)
                        .name("New Role")
                        .permissions(new HashSet<>(List.of(
                                "Permissions.Categories.View",
                                "Permissions.Products.View"
                        )))
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now().plusDays(20))
                        .build(),
                Role.builder()
                        .id(2L)
                        .name("New Role 2")
                        .permissions(new HashSet<>(List.of(
                                "Permissions.Orders.View",
                                "Permissions.Purchases.View"
                        )))
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now().plusDays(20))
                        .build()
        );
        var rolesDto = roles.stream().map(mapper::entityToDto).toList();

        Page<Role> page = new PageImpl<>(roles, Pageable.ofSize(pageSize), totalElementsInDb);
        var pagedList = mapper.pageToPagedList(page);

        assertNotNull(pagedList);
        assertEquals(pageNumber, pagedList.getPageNumber());
        assertEquals(pageSize, pagedList.getPageSize());
        assertEquals(totalElementsInDb, pagedList.getTotalElements());
        assertEquals(totalPages, pagedList.getTotalPages());
        assertFalse(pagedList.hasPreviousPage());
        assertTrue(pagedList.hasNextPage());
        assertThat(pagedList.getItems()).hasSameElementsAs(rolesDto);
    }
}
