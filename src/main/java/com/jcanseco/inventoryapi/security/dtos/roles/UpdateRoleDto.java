package com.jcanseco.inventoryapi.security.dtos.roles;

import lombok.*;

import java.util.List;

@EqualsAndHashCode
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleDto {
    private List<String> roleId;
    private String name;
    private List<String> actions;
}
