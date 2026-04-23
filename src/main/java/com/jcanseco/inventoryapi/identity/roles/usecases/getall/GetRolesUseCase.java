package com.jcanseco.inventoryapi.identity.roles.usecases.getall;

import com.jcanseco.inventoryapi.identity.roles.dto.GetRolesRequest;
import com.jcanseco.inventoryapi.identity.roles.dto.RoleDto;
import com.jcanseco.inventoryapi.identity.roles.domain.Role;
import com.jcanseco.inventoryapi.identity.roles.mapping.RoleMapper;
import com.jcanseco.inventoryapi.identity.roles.persistence.RoleRepository;
import com.jcanseco.inventoryapi.shared.pagination.PagedList;
import com.jcanseco.inventoryapi.shared.utils.IndexUtility;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import static com.jcanseco.inventoryapi.identity.roles.persistence.RoleSpecifications.byNameLike;
import static com.jcanseco.inventoryapi.identity.roles.persistence.RoleSpecifications.orderByCreatedAtAsc;
import static com.jcanseco.inventoryapi.identity.roles.persistence.RoleSpecifications.orderByCreatedAtDesc;
import static com.jcanseco.inventoryapi.identity.roles.persistence.RoleSpecifications.orderByIdAsc;
import static com.jcanseco.inventoryapi.identity.roles.persistence.RoleSpecifications.orderByIdDesc;
import static com.jcanseco.inventoryapi.identity.roles.persistence.RoleSpecifications.orderByNameAsc;
import static com.jcanseco.inventoryapi.identity.roles.persistence.RoleSpecifications.orderByNameDesc;
import static com.jcanseco.inventoryapi.identity.roles.persistence.RoleSpecifications.orderByUpdatedAtAsc;
import static com.jcanseco.inventoryapi.identity.roles.persistence.RoleSpecifications.orderByUpdatedAtDesc;

@Service
@RequiredArgsConstructor
public class GetRolesUseCase {

    private final RoleRepository roleRepository;
    private final RoleMapper mapper;
    private final IndexUtility indexUtility;

    @Transactional(readOnly = true)
    public List<RoleDto> execute(GetRolesRequest request) {
        var spec = composeSpecification(request);
        return roleRepository.findAll(spec)
                .stream()
                .map(mapper::entityToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PagedList<RoleDto> executePaged(GetRolesRequest request) {
        var pageNumber = indexUtility.toZeroBasedIndex(request.getPageNumber());
        var pageSize = request.getPageSize();
        var specification = composeSpecification(request);
        var pageRequest = PageRequest.of(pageNumber, pageSize);
        var page = roleRepository.findAll(specification, pageRequest);
        return mapper.pageToPagedList(page);
    }

    private Specification<Role> composeSpecification(GetRolesRequest request) {
        Specification<Role> spec = Specification.where(null);
        if (StringUtils.hasText(request.getName())) {
            spec = spec.and(byNameLike(request.getName()));
        }
        return orderBySpecifications(spec, request);
    }

    private Specification<Role> orderBySpecifications(Specification<Role> spec, GetRolesRequest request) {
        var orderBy = !StringUtils.hasText(request.getOrderBy()) ? "" : request.getOrderBy();
        var isAscending = indexUtility.isAscendingOrder(request.getSortOrder());
        return switch (orderBy) {
            case "id" -> isAscending ? orderByIdAsc(spec) : orderByIdDesc(spec);
            case "name" -> isAscending ? orderByNameAsc(spec) : orderByNameDesc(spec);
            case "createdAt" -> isAscending ? orderByCreatedAtAsc(spec) : orderByCreatedAtDesc(spec);
            case "updatedAt" -> isAscending ? orderByUpdatedAtAsc(spec) : orderByUpdatedAtDesc(spec);
            default -> isAscending ? orderByNameAsc(spec) : orderByNameDesc(spec);
        };
    }
}

