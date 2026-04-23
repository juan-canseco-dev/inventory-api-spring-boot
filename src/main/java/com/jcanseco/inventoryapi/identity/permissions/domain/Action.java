package com.jcanseco.inventoryapi.identity.permissions.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import static com.jcanseco.inventoryapi.identity.permissions.domain.Permissions.permissionOf;






@Data
@AllArgsConstructor
public class Action {

    private int order;
    private String resource;
    private String action;
    private String description;
    private boolean required;

    public String asPermission() {
        return permissionOf(resource, action);
    }

}





