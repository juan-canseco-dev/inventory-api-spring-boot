package com.jcanseco.inventoryapi.security.services;

import static com.jcanseco.inventoryapi.security.specifications.UserSpecifications.*;
import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.exceptions.DomainException;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.security.dtos.users.*;
import com.jcanseco.inventoryapi.security.entities.User;
import com.jcanseco.inventoryapi.security.mappers.UserMapper;
import com.jcanseco.inventoryapi.security.repositories.RoleRepository;
import com.jcanseco.inventoryapi.security.repositories.UserRepository;
import com.jcanseco.inventoryapi.security.specifications.UserSpecifications;
import com.jcanseco.inventoryapi.utils.IndexUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper mapper;
    private final IndexUtility indexUtility;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long createUser(CreateUserDto dto) {
        var role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new DomainException(String.format("Role with the Id : {%d} was not found.", dto.getRoleId())));

        var newUser = userRepository.saveAndFlush(
                User.builder()
                        .role(role)
                        .email(dto.getEmail())
                        .fullName(dto.getFullName())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .build()
        );

        return newUser.getId();
    }

    @Transactional
    public void updateUser(UpdateUserDto dto) {
        var user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException(String.format("User with the Id : {%d} was not found.", dto.getUserId())));

        user.setFullName(dto.getFullName());
        userRepository.saveAndFlush(user);
    }

    @Transactional
    public void changeUserRole(ChangeUserRoleDto dto) {

        var user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException(String.format("User with the Id : {%d} was not found.", dto.getUserId())));

        if (!user.getRole().getId().equals(dto.getRoleId())) {
            var role = roleRepository.findById(dto.getRoleId())
                    .orElseThrow(() -> new DomainException(String.format("Role with the Id : {%d} was not found.", dto.getRoleId())));
            user.setRole(role);
            userRepository.saveAndFlush(user);
        }
    }

    @Transactional
    public void deleteUser(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with the Id : {%d} was not found.", userId)));
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public UserDetailsDto getUserById(Long userId) {
        return  userRepository.findById(userId)
                .map(mapper::entityToDetailsDto)
                .orElseThrow(() -> new NotFoundException(String.format("User with the Id : {%d} was not found.", userId)));
    }

    private Specification<User> orderBySpecification(Specification<User> spec, GetUsersRequest request) {
        var orderBy = !StringUtils.hasText(request.getOrderBy())? "" : request.getOrderBy();
        var isAscending = indexUtility.isAscendingOrder(request.getSortOrder());
        return switch (orderBy) {
            case "id" -> isAscending? orderByIdAsc(spec) : orderByIdDesc(spec);
            case "fullName" -> isAscending? orderByFullNameAsc(spec) : orderByFullNameDesc(spec);
            case "email" -> isAscending? orderByEmailAsc(spec) : orderByEmailDesc(spec);
            case "createdAt" -> isAscending? orderByCreatedAtAsc(spec) : orderByCreatedAtDesc(spec);
            case "updatedAt" -> isAscending? orderByUpdatedAtAsc(spec) : orderByUpdatedAtDesc(spec);
            default -> isAscending? orderByFullNameAsc(spec) : orderByFullNameDesc(spec);
        };
    }

    public Specification<User> composeSpecification(GetUsersRequest request) {
        Specification<User> spec = Specification.where(null);
        if (StringUtils.hasText(request.getFullName())) {
            spec = spec.and(byFullNameLike(request.getFullName()));
        }
        if (StringUtils.hasText(request.getEmail())) {
            spec = spec.and(byEmailLike(request.getEmail()));
        }
        return orderBySpecification(spec, request);
    }


    @Transactional(readOnly = true)
    public List<UserDto> getUsers(GetUsersRequest request) {
        var spec = composeSpecification(request);
        return userRepository.findAll(spec)
                .stream()
                .map(mapper::entityToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PagedList<UserDto> getUsersPage(GetUsersRequest request) {
        var pageNumber = indexUtility.toZeroBasedIndex(request.getPageNumber());
        var pageSize = request.getPageSize();
        var specification = composeSpecification(request);
        var pageRequest = PageRequest.of(pageNumber, pageSize);
        var page = userRepository.findAll(specification, pageRequest);
        return mapper.pageToPagedList(page);
    }

    public UserDetailsService userDetailsService() {
        return username -> userRepository.findOne(UserSpecifications.byEmail(username))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User : {%s} not found.", username)));
    }
}
