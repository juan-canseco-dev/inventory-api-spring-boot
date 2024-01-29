package com.jcanseco.inventoryapi.security.resources;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Action {

    private int order;
    private String resource;
    private String action;
    private String description;
    private boolean required;

    public static String permissionOf(String resource, String action) {
        return String.format("Permissions.%s.%s", resource, action);
    }
}
