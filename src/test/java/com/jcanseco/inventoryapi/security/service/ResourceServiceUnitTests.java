package com.jcanseco.inventoryapi.security.service;

import com.jcanseco.inventoryapi.security.services.ResourceService;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class ResourceServiceUnitTests {
    private final ResourceService service = new ResourceService();

    @Test
    public void groupPermissionsWithResourceShouldReturnExpected() {

        Map<String, List<String>> expectedResourceWithPermissions = new HashMap<>() {{
            put("Categories", List.of(
                    "Permissions.Categories.View",
                    "Permissions.Categories.Create"
            ));
            put("Products", List.of(
                    "Permissions.Products.View",
                    "Permissions.Products.Create"
            ));
        }};

        var permissions = List.of(
          "Permissions.Categories.View",
          "Permissions.Categories.Create",
          "Permissions.Products.View",
          "Permissions.Products.Create"
        );
        var resourceWithPermissions = service.groupPermissionsWithResource(permissions);
        assertEquals(expectedResourceWithPermissions, resourceWithPermissions);
    }

    @Test
    public void getInvalidPermissionsShouldReturnExpected() {
        var expectedInvalidPermissions = List.of(
                "Permissions.InvalidModule.View",
                "Permissions.InvalidModule.Create"
        );
        var permissions = List.of(
                "Permissions.Categories.View",
                "Permissions.Categories.Create",
                "Permissions.Products.View",
                "Permissions.Products.Create",
                "Permissions.InvalidModule.View",
                "Permissions.InvalidModule.Create"
        );
        var invalidPermissions = service.getInvalidPermissions(permissions);
        assertEquals(expectedInvalidPermissions, invalidPermissions);
    }

    @Test
    public void hasRequiredPermissionsByResourceShouldReturnTrue() {
        var permissions = List.of(
                "Permissions.Categories.View",
                "Permissions.Categories.Create",
                "Permissions.Categories.Update"
        );
        var hasRequiredPermissions = service.hasRequiredPermissionsByResource("Categories", permissions);
        assertTrue(hasRequiredPermissions);
    }

    @Test
    public void hasRequiredPermissionsByResourceShouldReturnFalse() {
        var permissions = List.of(
                "Permissions.Categories.Create",
                "Permissions.Categories.Update"
        );
        var hasRequiredPermissions = service.hasRequiredPermissionsByResource("Categories", permissions);
        assertFalse(hasRequiredPermissions);
    }

    @Test
    public void hasPermissionsWhenPermissionsExistsShouldReturnTrue() {
        var permissions = List.of(
                "Permissions.Categories.View",
                "Permissions.Categories.Create",
                "Permissions.Products.View",
                "Permissions.Products.Create"
        );
        var hasPermissions = service.hasPermissions(permissions);
        assertTrue(hasPermissions);
    }

    @Test
    public void hasPermissionsWhenSomePermissionsNotExistsShouldReturnFalse() {
        var permissions = List.of(
                "Permissions.Categories.View",
                "Permissions.Categories.Create",
                "Permissions.Products.View",
                "Permissions.Products.Create",
                "Permissions.InvalidModule.View",
                "Permissions.InvalidModule.Create"
        );
        var hasPermissions = service.hasPermissions(permissions);
        assertFalse(hasPermissions);
    }
}
