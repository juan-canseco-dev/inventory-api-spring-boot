package com.jcanseco.inventoryapi.identity.roles.mapping;

import com.jcanseco.inventoryapi.identity.roles.dto.RoleDetailsDto;
import com.jcanseco.inventoryapi.identity.roles.dto.RoleDto;
import com.jcanseco.inventoryapi.identity.roles.domain.Role;
import com.jcanseco.inventoryapi.shared.pagination.PagedList;
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






