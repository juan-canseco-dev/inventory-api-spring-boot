package com.jcanseco.inventoryapi.security.resources;

import org.springframework.stereotype.Component;

@Component("Permissions")
public final class Permissions {

    public static String permissionOf(String resource, String action) {
        return String.format("Permissions.%s.%s", resource, action);
    }

}
