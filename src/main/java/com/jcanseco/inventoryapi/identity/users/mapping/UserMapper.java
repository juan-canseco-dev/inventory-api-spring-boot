package com.jcanseco.inventoryapi.identity.users.mapping;

import com.jcanseco.inventoryapi.identity.roles.domain.Role;
import com.jcanseco.inventoryapi.identity.users.dto.UserDetailsDto;
import com.jcanseco.inventoryapi.identity.users.dto.UserDto;
import com.jcanseco.inventoryapi.identity.users.domain.User;
import com.jcanseco.inventoryapi.shared.pagination.PagedList;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;





@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto entityToDto(User user);

    UserDetailsDto entityToDetailsDto(User user);

    default String roleToName(Role role) {
        return role.getName();
    }

    default PagedList<UserDto> pageToPagedList(Page<User> page) {
        return new PagedList<>(
                page.get().map(this::entityToDto).toList(),
                page.getPageable().getPageNumber() + 1,
                page.getPageable().getPageSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

}






