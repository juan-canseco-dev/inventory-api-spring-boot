package com.jcanseco.inventoryapi.security.services;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.exceptions.DomainException;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.security.dtos.roles.*;
import com.jcanseco.inventoryapi.security.mappers.RoleMapper;
import com.jcanseco.inventoryapi.security.entities.Role;
import com.jcanseco.inventoryapi.security.repositories.RoleRepository;
import com.jcanseco.inventoryapi.utils.IndexUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import static com.jcanseco.inventoryapi.security.specifications.RoleSpecifications.*;

@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final ResourceService resourceService;
    private final RoleMapper mapper;
    private final IndexUtility indexUtility;

    private void validatePermissions(List<String> permissions) {
        if (!resourceService.hasPermissions(permissions)) {
            var invalidPermissions = resourceService.getInvalidPermissions(permissions);
            throw new DomainException(
                    String.format("The following permissions are invalid: %s", String.join(", ", invalidPermissions))
            );
        }

        Map<String, List<String>> resourceWithPermissions = resourceService.groupPermissionsWithResource(permissions);

        for (Map.Entry<String, List<String>> entry : resourceWithPermissions.entrySet()) {
            if (!resourceService.hasRequiredPermissionsByResource(entry.getKey(), entry.getValue())) {
                throw new DomainException("The required permissions for the resource are not selected.");
            }
        }
    }

    @Transactional
    public Long createRole(CreateRoleDto dto) {
        validatePermissions(dto.getPermissions());
        var newRole = roleRepository.saveAndFlush(
                Role.builder()
                        .name(dto.getName())
                        .permissions(new HashSet<>(dto.getPermissions()))
                        .build()
        );
        return newRole.getId();
    }

    @Transactional
    public void updateRole(UpdateRoleDto dto) {

        var role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new NotFoundException(String.format("Role with the Id : {%d} was not found.", dto.getRoleId())));

        validatePermissions(dto.getPermissions());

        role.setName(dto.getName());
        role.setPermissions(new HashSet<>(dto.getPermissions()));

        roleRepository.saveAndFlush(role);
    }

    @Transactional
    public void deleteRole(Long roleId) {
        var role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException(String.format("Role with the Id : {%d} was not found.", roleId)));
        roleRepository.delete(role);
    }

    @Transactional(readOnly = true)
    public RoleDetailsDto getRoleById(Long roleId) {
        return roleRepository.findById(roleId)
                .map(mapper::entityToDetailsDto)
                .orElseThrow(() -> new NotFoundException(String.format("Role with the Id : {%d} was not found.", roleId)));
    }

    private Specification<Role> orderBySpecifications(Specification<Role> spec, GetRolesRequest request) {
        var orderBy = !StringUtils.hasText(request.getOrderBy())? "" : request.getOrderBy();
        var isAscending = indexUtility.isAscendingOrder(request.getSortOrder());
        return switch (orderBy) {
            case "id" -> isAscending? orderByIdAsc(spec) : orderByIdDesc(spec);
            case "name" -> isAscending? orderByNameAsc(spec) : orderByNameDesc(spec);
            case "createdAt" -> isAscending? orderByCreatedAtAsc(spec) : orderByCreatedAtDesc(spec);
            case "updatedAt" -> isAscending? orderByUpdatedAtAsc(spec) : orderByUpdatedAtDesc(spec);
            default -> isAscending? orderByNameAsc(spec) : orderByNameDesc(spec);
        };
    }

    public Specification<Role> composeSpecification(GetRolesRequest request) {
        Specification<Role> spec = Specification.where(null);
        if (StringUtils.hasText(request.getName())) {
            spec = spec.and(byNameLike(request.getName()));
        }
        return orderBySpecifications(spec, request);
    }

    @Transactional(readOnly = true)
    public List<RoleDto> getRoles(GetRolesRequest request) {
        var spec = composeSpecification(request);
        return roleRepository.findAll(spec)
                .stream()
                .map(mapper::entityToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PagedList<RoleDto> getRolesPage(GetRolesRequest request) {
        var pageNumber = indexUtility.toZeroBasedIndex(request.getPageNumber());
        var pageSize = request.getPageSize();

        var specification = composeSpecification(request);
        var pageRequest = PageRequest.of(pageNumber, pageSize);
        var page = roleRepository.findAll(specification, pageRequest);

        return mapper.pageToPagedList(page);
    }
}
