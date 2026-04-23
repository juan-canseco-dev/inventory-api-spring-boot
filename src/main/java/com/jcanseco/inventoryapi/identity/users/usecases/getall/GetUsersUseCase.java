package com.jcanseco.inventoryapi.identity.users.usecases.getall;

import com.jcanseco.inventoryapi.identity.users.dto.GetUsersRequest;
import com.jcanseco.inventoryapi.identity.users.dto.UserDto;
import com.jcanseco.inventoryapi.identity.users.application.AuthenticatedUserService;
import com.jcanseco.inventoryapi.identity.users.domain.User;
import com.jcanseco.inventoryapi.identity.users.mapping.UserMapper;
import com.jcanseco.inventoryapi.identity.users.persistence.UserRepository;
import com.jcanseco.inventoryapi.shared.pagination.PagedList;
import com.jcanseco.inventoryapi.shared.utils.IndexUtility;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import static com.jcanseco.inventoryapi.identity.users.persistence.UserSpecifications.byEmailLike;
import static com.jcanseco.inventoryapi.identity.users.persistence.UserSpecifications.byFullNameLike;
import static com.jcanseco.inventoryapi.identity.users.persistence.UserSpecifications.excludeCurrentUser;
import static com.jcanseco.inventoryapi.identity.users.persistence.UserSpecifications.orderByCreatedAtAsc;
import static com.jcanseco.inventoryapi.identity.users.persistence.UserSpecifications.orderByCreatedAtDesc;
import static com.jcanseco.inventoryapi.identity.users.persistence.UserSpecifications.orderByEmailAsc;
import static com.jcanseco.inventoryapi.identity.users.persistence.UserSpecifications.orderByEmailDesc;
import static com.jcanseco.inventoryapi.identity.users.persistence.UserSpecifications.orderByFullNameAsc;
import static com.jcanseco.inventoryapi.identity.users.persistence.UserSpecifications.orderByFullNameDesc;
import static com.jcanseco.inventoryapi.identity.users.persistence.UserSpecifications.orderByIdAsc;
import static com.jcanseco.inventoryapi.identity.users.persistence.UserSpecifications.orderByIdDesc;
import static com.jcanseco.inventoryapi.identity.users.persistence.UserSpecifications.orderByUpdatedAtAsc;
import static com.jcanseco.inventoryapi.identity.users.persistence.UserSpecifications.orderByUpdatedAtDesc;

@Service
@RequiredArgsConstructor
public class GetUsersUseCase {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final IndexUtility indexUtility;
    private final AuthenticatedUserService authenticatedUserService;

    @Transactional(readOnly = true)
    public List<UserDto> execute(GetUsersRequest request) {
        var spec = composeSpecification(request);
        return userRepository.findAll(spec)
                .stream()
                .map(mapper::entityToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PagedList<UserDto> executePaged(GetUsersRequest request) {
        var pageNumber = indexUtility.toZeroBasedIndex(request.getPageNumber());
        var pageSize = request.getPageSize();
        var specification = composeSpecification(request);
        var pageRequest = PageRequest.of(pageNumber, pageSize);
        var page = userRepository.findAll(specification, pageRequest);
        return mapper.pageToPagedList(page);
    }

    private Specification<User> composeSpecification(GetUsersRequest request) {
        Long currentUserId = authenticatedUserService.getCurrentUser().getId();
        Specification<User> spec = Specification.where(null);
        spec = spec.and(excludeCurrentUser(currentUserId));

        if (StringUtils.hasText(request.getFullName())) {
            spec = spec.and(byFullNameLike(request.getFullName()));
        }
        if (StringUtils.hasText(request.getEmail())) {
            spec = spec.and(byEmailLike(request.getEmail()));
        }
        return orderBySpecification(spec, request);
    }

    private Specification<User> orderBySpecification(Specification<User> spec, GetUsersRequest request) {
        var orderBy = !StringUtils.hasText(request.getOrderBy()) ? "" : request.getOrderBy();
        var isAscending = indexUtility.isAscendingOrder(request.getSortOrder());
        return switch (orderBy) {
            case "id" -> isAscending ? orderByIdAsc(spec) : orderByIdDesc(spec);
            case "fullName" -> isAscending ? orderByFullNameAsc(spec) : orderByFullNameDesc(spec);
            case "email" -> isAscending ? orderByEmailAsc(spec) : orderByEmailDesc(spec);
            case "createdAt" -> isAscending ? orderByCreatedAtAsc(spec) : orderByCreatedAtDesc(spec);
            case "updatedAt" -> isAscending ? orderByUpdatedAtAsc(spec) : orderByUpdatedAtDesc(spec);
            default -> isAscending ? orderByFullNameAsc(spec) : orderByFullNameDesc(spec);
        };
    }
}

