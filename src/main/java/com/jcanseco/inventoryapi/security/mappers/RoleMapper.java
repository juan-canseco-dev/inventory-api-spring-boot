package com.jcanseco.inventoryapi.security.mappers;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.security.dtos.roles.RoleDetailsDto;
import com.jcanseco.inventoryapi.security.dtos.roles.RoleDto;
import com.jcanseco.inventoryapi.security.entities.Role;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDto entityToDto(Role role);
    RoleDetailsDto entityToDetailsDto(Role role);
    default PagedList<RoleDto> pageToPagedList(Page<Role> page) {
        return new PagedList<>(
                page.get().map(this::entityToDto).toList(),
                page.getPageable().getPageNumber() + 1,
                page.getPageable().getPageSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }
}
