package com.jcanseco.inventoryapi.security.mappers;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.security.dtos.users.UserDetailsDto;
import com.jcanseco.inventoryapi.security.dtos.users.UserDto;
import com.jcanseco.inventoryapi.security.entities.Role;
import com.jcanseco.inventoryapi.security.entities.User;
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
