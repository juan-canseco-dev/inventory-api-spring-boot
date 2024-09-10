package com.jcanseco.inventoryapi.security.service;

import com.jcanseco.inventoryapi.exceptions.DomainException;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.security.dtos.users.ChangeUserRoleDto;
import com.jcanseco.inventoryapi.security.dtos.users.CreateUserDto;
import com.jcanseco.inventoryapi.security.dtos.users.GetUsersRequest;
import com.jcanseco.inventoryapi.security.dtos.users.UpdateUserDto;
import com.jcanseco.inventoryapi.security.entities.Role;
import com.jcanseco.inventoryapi.security.entities.User;
import com.jcanseco.inventoryapi.security.mappers.UserMapper;
import com.jcanseco.inventoryapi.security.repositories.RoleRepository;
import com.jcanseco.inventoryapi.security.repositories.UserRepository;
import com.jcanseco.inventoryapi.security.services.UserService;
import com.jcanseco.inventoryapi.utils.IndexUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
public class UserServiceUnitTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Spy
    private UserMapper mapper = Mappers.getMapper(UserMapper.class);
    @Spy
    private IndexUtility indexUtility = new IndexUtility();
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService service;
    private final Long userId = 1L;
    private final Long roleId = 2L;
    @Captor
    private ArgumentCaptor<User> userArgCaptor;

    @Test
    public void createUserWhenRoleExistShouldCreate() {
        var dto = CreateUserDto.builder()
                .fullName("Jane Doe")
                .roleId(1L)
                .email("jane.doe@mail.com")
                .password("password.1234")
                .build();

        when(roleRepository.findById(1L)).thenReturn(Optional.of(mock()));
        when(passwordEncoder.encode(any())).thenReturn("mockPassword");
        when(userRepository.saveAndFlush(any())).thenReturn(User.builder().id(1L).build());

        var result = service.createUser(dto);
        assertEquals(1L, result);
    }

    @Test
    public void createUserWhenRoleNotExistsShouldThrowException() {
        var dto = CreateUserDto.builder()
                .fullName("Jane Doe")
                .roleId(roleId)
                .email("jane.doe@mail.com")
                .password("password.1234")
                .build();

        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());
        assertThrows(DomainException.class, () -> service.createUser(dto));
    }

    @Test
    public void updateUserWhenUserExistsShouldUpdate() {

        var expectedFullName = "Updated FullName";

        var dto = UpdateUserDto.builder()
                .userId(userId)
                .fullName("Updated FullName")
                .build();

        var fakeUser = User.builder()
                .id(userId)
                .fullName("FullName")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(fakeUser));
        when(userRepository.saveAndFlush(any())).thenReturn(any());

        service.updateUser(dto);

        verify(userRepository, times(1)).saveAndFlush(any());
        verify(userRepository).saveAndFlush(userArgCaptor.capture());

        var updatedUser = userArgCaptor.getValue();
        assertNotNull(updatedUser);
        assertEquals(expectedFullName, updatedUser.getFullName());
    }

    @Test
    public void updateUserWhenUserNotExistsShouldThrowException() {
        var dto = UpdateUserDto.builder()
                .userId(userId)
                .fullName("Updated FullName")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.updateUser(dto));
    }

    @Test
    public void changeUserRoleWhenUserExistsAndRoleExistsShouldChangeRole() {
        var updatedRoleId = 3L;
        var updatedRole = Role.builder()
                .id(updatedRoleId)
                .name("Updated Role")
                .permissions(new HashSet<>(List.of(
                        "Permissions.Dashboard.View",
                        "Permissions.Categories.View",
                        "Permissions.Categories.Create"
                )))
                .build();

        var foundRole = Role.builder()
                .id(roleId)
                .name("Role Name")
                .permissions(new HashSet<>(List.of(
                        "Permissions.Dashboard.View"
                )))
                .build();

        var foundUser = User.builder()
                .id(userId)
                .fullName("FullName")
                .role(foundRole)
                .build();

        var dto = ChangeUserRoleDto.builder()
                .userId(userId)
                .roleId(updatedRoleId)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(foundUser));
        when(roleRepository.findById(updatedRoleId)).thenReturn(Optional.of(updatedRole));
        when(userRepository.saveAndFlush(any())).thenReturn(any());

        service.changeUserRole(dto);

        verify(userRepository, times(1)).saveAndFlush(any());
        verify(userRepository).saveAndFlush(userArgCaptor.capture());

        var updatedUser = userArgCaptor.getValue();
        assertNotNull(updatedUser);
        assertEquals(updatedRole, updatedUser.getRole());
    }

    @Test
    public void changeUserRoleWhenUserNotExistsShouldThrowException() {
        var dto = ChangeUserRoleDto.builder()
                .userId(userId)
                .roleId(roleId)
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.changeUserRole(dto));
    }

    @Test
    public void changeUserRoleWhenRoleNotExistsShouldThrowException() {

        var updatedRoleId = 3L;

        var foundRole = Role.builder()
                .id(roleId)
                .name("Role Name")
                .permissions(new HashSet<>(List.of(
                        "Permissions.Dashboard.View"
                )))
                .build();

        var foundUser = User.builder()
                .id(userId)
                .fullName("FullName")
                .role(foundRole)
                .build();

        var dto = ChangeUserRoleDto.builder()
                .userId(userId)
                .roleId(updatedRoleId)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(foundUser));
        when(roleRepository.findById(updatedRoleId)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> service.changeUserRole(dto));
    }

    @Test
    public void deleteUserWhenUserExistsShouldDelete() {
        var foundRole = Role.builder()
                .id(roleId)
                .name("Role Name")
                .permissions(new HashSet<>(List.of(
                        "Permissions.Dashboard.View"
                )))
                .build();

        var foundUser = User.builder()
                .id(userId)
                .fullName("FullName")
                .role(foundRole)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(foundUser));
        doNothing().when(userRepository).delete(foundUser);
        service.deleteUser(userId);
        verify(userRepository, times(1)).delete(foundUser);
    }

    @Test
    public void deleteUserWhenUserNotExistsShouldThrowException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.deleteUser(userId));
    }

    @Test
    public void getUserByIdWhenUserExistsShouldReturnUser() {
        var foundRole = Role.builder()
                .id(roleId)
                .name("Role Name")
                .permissions(new HashSet<>(List.of(
                        "Permissions.Dashboard.View"
                )))
                .createdAt(LocalDateTime.now().plusMonths(5))
                .updatedAt(LocalDateTime.now().plusMonths(6))
                .build();

        var foundUser = User.builder()
                .id(userId)
                .fullName("FullName")
                .role(foundRole)
                .email("jane.doe@mail.com")
                .createdAt(LocalDateTime.now().plusMonths(5))
                .updatedAt(LocalDateTime.now().plusMonths(6))
                .build();

        var expectedDto = mapper.entityToDetailsDto(foundUser);
        when(userRepository.findById(userId)).thenReturn(Optional.of(foundUser));

        var resultDto = service.getUserById(userId);
        assertEquals(expectedDto, resultDto);
    }

    @Test
    public void getUserByIdWhenUserNotExistsShouldThrowException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getUserById(userId));
    }

    @Test
    public void getUsersShouldReturnList() {
        var foundRole = Role.builder()
                .id(roleId)
                .name("Role Name")
                .permissions(new HashSet<>(List.of(
                        "Permissions.Dashboard.View"
                )))
                .createdAt(LocalDateTime.now().plusMonths(5))
                .updatedAt(LocalDateTime.now().plusMonths(6))
                .build();

        var foundUser = User.builder()
                .id(userId)
                .fullName("FullName")
                .role(foundRole)
                .email("jane.doe@mail.com")
                .createdAt(LocalDateTime.now().plusMonths(5))
                .updatedAt(LocalDateTime.now().plusMonths(6))
                .build();

        var users = List.of(foundUser);
        var expectedResult = users.stream().map(mapper::entityToDto).toList();

        var request = GetUsersRequest.builder().build();
        Specification<User> spec = any(Specification.class);

        when(userRepository.findAll(spec)).thenReturn(users);

        var result = service.getUsers(request);
        assertEquals(expectedResult, result);
    }

    @Test
    public void getUsersPageShouldReturnPage() {

        var foundRole = Role.builder()
                .id(roleId)
                .name("Role Name")
                .permissions(new HashSet<>(List.of(
                        "Permissions.Dashboard.View"
                )))
                .createdAt(LocalDateTime.now().plusMonths(5))
                .updatedAt(LocalDateTime.now().plusMonths(6))
                .build();

        var foundUser = User.builder()
                .id(userId)
                .fullName("FullName")
                .role(foundRole)
                .email("jane.doe@mail.com")
                .createdAt(LocalDateTime.now().plusMonths(5))
                .updatedAt(LocalDateTime.now().plusMonths(6))
                .build();

        var totalRolesInDb = 2;
        var totalPages = 2;

        var users = List.of(
                foundUser
        );
        var expectedItems =  users.stream().map(mapper::entityToDto).toList();


        var request = GetUsersRequest.builder()
                .pageNumber(1)
                .pageSize(1)
                .build();

        Specification<User> mockSpec = any(Specification.class);
        PageRequest mockPageRequest = any();
        Page<User> mockPage = new PageImpl<>(
                users,
                Pageable.ofSize(1),
                totalRolesInDb
        );

        when(userRepository.findAll(mockSpec, mockPageRequest)).thenReturn(mockPage);

        var pagedList = service.getUsersPage(request);
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
