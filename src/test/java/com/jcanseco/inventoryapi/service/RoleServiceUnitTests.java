package com.jcanseco.inventoryapi.service;

import com.jcanseco.inventoryapi.exceptions.DomainException;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.security.dtos.roles.CreateRoleDto;
import com.jcanseco.inventoryapi.security.dtos.roles.GetRolesRequest;
import com.jcanseco.inventoryapi.security.dtos.roles.UpdateRoleDto;
import com.jcanseco.inventoryapi.security.entities.Role;
import com.jcanseco.inventoryapi.security.mappers.RoleMapper;
import com.jcanseco.inventoryapi.security.repositories.RoleRepository;
import com.jcanseco.inventoryapi.security.services.ResourceService;
import com.jcanseco.inventoryapi.security.services.RoleService;
import com.jcanseco.inventoryapi.utils.IndexUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
public class RoleServiceUnitTests {
    @Spy
    private ResourceService resourceService = new ResourceService();
    @Mock
    private RoleRepository repository;
    @Spy
    private RoleMapper mapper = Mappers.getMapper(RoleMapper.class);
    @Spy
    private IndexUtility indexUtility = new IndexUtility();
    @InjectMocks
    private RoleService service;
    @Captor
    private ArgumentCaptor<Role> roleArgCaptor;
    private final Long roleId = 1L;
    private Role role;

    @BeforeEach
    public void setup() {
        role = Role
                .builder()
                .id(roleId)
                .name("New Role")
                .permissions(new HashSet<>(List.of(
                        "Permissions.Users.View",
                        "Permissions.Products.View"
                )))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now().plusDays(7))
                .build();
    }


    @Test
    public void createRoleWhenPermissionsAreValidShouldCreate() {

        var dto = CreateRoleDto.builder()
                .name("New Role")
                .permissions(List.of(
                "Permissions.Users.View",
                "Permissions.Products.View"
                ))
                .build();

        when(repository.saveAndFlush(any())).thenReturn(role);

        var newRoleId = service.createRole(dto);
        assertEquals(roleId, newRoleId);
    }

    @Test
    public void createRoleWhenPermissionsAreInvalidShouldThrowException() {
        var dto = CreateRoleDto.builder()
                .name("New Role")
                .permissions(List.of(
                        "Permissions.Users.View",
                        "Permissions.Products.NonExistentPermission"
                ))
                .build();

        assertThrows(DomainException.class, () -> service.createRole(dto));
    }

    @Test
    public void createRoleWhenRequiredPermissionsAreNotPresentShouldThrowException() {
        var dto = CreateRoleDto.builder()
                .name("New Role")
                .permissions(List.of(
                        "Permissions.Users.View",
                        "Permissions.Products.Create"
                ))
                .build();

        assertThrows(DomainException.class, () -> service.createRole(dto));
    }

    @Test
    public void updateRoleWhenPermissionsAreValidShouldUpdate() {

        var expectedName = "Updated Role";

        var expectedPermissions = List.of(
                "Permissions.Users.View",
                "Permissions.Products.View",
                "Permissions.Products.Create"
        );

        var dto = UpdateRoleDto.builder()
                .roleId(roleId)
                .name(expectedName)
                .permissions(expectedPermissions)
                .build();

        when(repository.findById(roleId)).thenReturn(Optional.of(role));
        when(repository.saveAndFlush(any())).thenReturn(any());

        service.updateRole(dto);

        verify(repository, times(1)).saveAndFlush(any());
        verify(repository).saveAndFlush(roleArgCaptor.capture());

        var updatedRole = roleArgCaptor.getValue();
        assertNotNull(updatedRole);
        assertEquals(expectedName, updatedRole.getName());
        assertThat(updatedRole.getPermissions()).hasSameElementsAs(expectedPermissions);
    }

    @Test
    public void updateRoleWhenPermissionsAreNotValidShouldThrowException() {

        var permissions = List.of(
                "Permissions.Users.View",
                "Permissions.Products.Create",
                "Permissions.Products.InvalidPermissions"
        );

        var dto = UpdateRoleDto.builder()
                .roleId(roleId)
                .name("Updated Role")
                .permissions(permissions)
                .build();

        when(repository.findById(roleId)).thenReturn(Optional.of(role));
        assertThrows(DomainException.class, () -> service.updateRole(dto));
    }

    @Test
    public void updateRoleWhenRequiredPermissionsAreNotPresentShouldThrowException() {
        var permissions = List.of(
                "Permissions.Users.Create"
        );

        var dto = UpdateRoleDto.builder()
                .roleId(roleId)
                .name("Updated Role")
                .permissions(permissions)
                .build();

        when(repository.findById(roleId)).thenReturn(Optional.of(role));
        assertThrows(DomainException.class, () -> service.updateRole(dto));
    }

    @Test
    public void updateRoleWhenRoleNotExistsShouldThrowException() {

        var permissions = List.of(
                "Permissions.Users.View",
                "Permissions.Products.View"
        );

        var dto = UpdateRoleDto.builder()
                .roleId(roleId)
                .name("Updated Role")
                .permissions(permissions)
                .build();

        when(repository.findById(roleId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.updateRole(dto));
    }

    @Test
    public void deleteRoleWhenRoleExistsShouldDelete() {
        when(repository.findById(roleId)).thenReturn(Optional.of(role));
        doNothing().when(repository).delete(role);
        service.deleteRole(roleId);
        verify(repository, times(1)).delete(role);
    }

    @Test
    public void deleteRoleWhenRoleNotExistsShouldThrowException() {
        when(repository.findById(roleId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.deleteRole(roleId));
    }

    @Test
    public void getRoleWhenRoleExistsShouldReturnRole() {
        when(repository.findById(roleId)).thenReturn(Optional.of(role));
        var expectedDto = mapper.entityToDetailsDto(role);
        var result = service.getRoleById(roleId);
        assertEquals(expectedDto, result);
    }

    @Test
    public void getRoleWhenRoleNotExistsShouldThrowException() {
        when(repository.findById(roleId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getRoleById(roleId));
    }

    @Test
    public void getRolesShouldReturnList() {

        var roles = List.of(
                role
        );

        var expectedResult = roles.stream().map(mapper::entityToDto).toList();
        var request = GetRolesRequest.builder().build();
        Specification<Role> spec = any(Specification.class);

        when(repository.findAll(spec)).thenReturn(roles);

        var result = service.getRoles(request);
        assertEquals(expectedResult, result);
    }

    @Test
    public void getRolesPageShouldReturnPagedList() {

        var totalRolesInDb = 2;
        var totalPages = 2;

        var roles = List.of(
                role
        );
        var expectedItems =  roles.stream().map(mapper::entityToDto).toList();


        var request = GetRolesRequest.builder()
                .pageNumber(1)
                .pageSize(1)
                .build();

        Specification<Role> mockSpec = any(Specification.class);
        PageRequest mockPageRequest = any();
        Page<Role> mockPage = new PageImpl<>(
                roles,
                Pageable.ofSize(1),
                totalRolesInDb
        );

        when(repository.findAll(mockSpec, mockPageRequest)).thenReturn(mockPage);

        var pagedList = service.getRolesPage(request);
        assertNotNull(pagedList);
        assertEquals(request.getPageNumber(), pagedList.getPageNumber());
        assertEquals(request.getPageSize(), pagedList.getPageSize());
        assertEquals(totalRolesInDb, pagedList.getTotalElements());
        assertEquals(totalPages, pagedList.getTotalPages());
        assertFalse(pagedList.hasPreviousPage());
        assertTrue(pagedList.hasNextPage());
        assertThat(pagedList.getItems()).hasSameElementsAs(expectedItems);
    }
}
