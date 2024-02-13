package com.jcanseco.inventoryapi.security.resources;

import lombok.AllArgsConstructor;
import lombok.Data;

import static com.jcanseco.inventoryapi.security.resources.Permissions.permissionOf;

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
