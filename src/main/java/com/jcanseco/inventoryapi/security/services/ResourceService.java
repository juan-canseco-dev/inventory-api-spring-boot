package com.jcanseco.inventoryapi.security.services;

import com.jcanseco.inventoryapi.security.resources.Action;
import com.jcanseco.inventoryapi.security.resources.Resource;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jcanseco.inventoryapi.security.resources.ActionType.*;
import static com.jcanseco.inventoryapi.security.resources.ResourceType.*;

@Service
@NoArgsConstructor
public class ResourceService {

    public Map<String, List<String>> groupPermissionsWithResource(List<String> permissions) {
        return getActionsStream()
                .filter(action -> permissions.contains(action.asPermission()))
                .collect(Collectors.groupingBy(
                        Action::getResource,
                        Collectors.mapping(Action::asPermission, Collectors.toList())
                ));
    }


    public List<String> getInvalidPermissions(List<String> permissions) {
        HashSet<String> allPermissions = allPermissions();
        return permissions
                .stream().filter(p -> !allPermissions.contains(p))
                .toList();
    }

    public boolean hasRequiredPermissionsByResource(String resource, List<String> permissions) {

        Map<String, HashSet<String>> resourceWithRequiredPermissions = getActionsStream()
                .filter(Action::isRequired)
                .collect(Collectors.groupingBy(
                        Action::getResource,
                        Collectors.mapping(Action::asPermission, Collectors.toCollection(HashSet::new))
                ));

        if (resourceWithRequiredPermissions.containsKey(resource)) {
            return resourceWithRequiredPermissions.get(resource).containsAll(permissions);
        }

        return false;
    }

    public boolean hasPermissions(List<String> permissions) {
        return allPermissions().containsAll(permissions);
    }

    public HashSet<String> allPermissions() {
        return getAllResources().stream()
                .flatMap(role -> role.getActions().stream().map(Action::asPermission))
                .collect(Collectors.toCollection(HashSet::new));
    }

    private Stream<Action> getActionsStream() {
        return getAllResources()
                .stream()
                .flatMap(r -> r.getActions().stream());
    }

    public List<Resource> getAllResources() {
        return List.of(
                new Resource(1, Dashboard, List.of(
                        new Action(1, Dashboard, View, "View Dashboard", true)
                )),
                new Resource(2, Users, List.of(
                        new Action(1, Users, View, "View Users", true),
                        new Action(2, Users, Create, "Create Users", false),
                        new Action(3, Users, Update, "Update Users", false),
                        new Action(4, Users, Delete, "Delete Users", false),
                        new Action(5, Users, Export, "Export Users", false),
                        new Action(6, Users, ChangeRole, "Change Users Role", false)
                )),
                new Resource(3, Roles, List.of(
                        new Action(1, Roles, View, "View Roles", true),
                        new Action(2, Roles, Create, "Create Roles", false),
                        new Action(3, Roles, Update, "Update Roles", false),
                        new Action(4, Roles, Delete, "Delete Roles", false)
                )),
                new Resource(4, UnitsOfMeasurement, List.of(
                        new Action(1, UnitsOfMeasurement, View, "View Units Of Measurement", true),
                        new Action(2, UnitsOfMeasurement, Create, "Create Units Of Measurement", false),
                        new Action(3, UnitsOfMeasurement, Update, "Update Units Of Measurement", false),
                        new Action(4, UnitsOfMeasurement, Delete, "Delete Units Of Measurement", false)
                )),
                new Resource(5, Categories, List.of(
                        new Action(1, Categories, View, "View Categories", true),
                        new Action(2, Categories, Create, "Create Categories", false),
                        new Action(3, Categories, Update, "Update Categories", false),
                        new Action(4, Categories, Delete, "Delete Units Categories", false)
                )),
                new Resource(6, Suppliers, List.of(
                        new Action(1, Suppliers, View, "View Suppliers", true),
                        new Action(2, Suppliers, Create, "Create Suppliers", false),
                        new Action(3, Suppliers, Update, "Update Suppliers", false),
                        new Action(4, Suppliers, Delete, "Delete Suppliers", false)
                )),
                new Resource(7, Products, List.of(
                        new Action(1, Products, View, "View Products", true),
                        new Action(2, Products, Create, "Create Products", false),
                        new Action(3, Products, Update, "Update Products", false),
                        new Action(4, Products, Delete, "Delete Products", false)
                )),
                new Resource(8, Purchases, List.of(
                        new Action(1, Purchases, View, "View Purchases", true),
                        new Action(2, Purchases, Create, "Create Purchases", false),
                        new Action(3, Purchases, Update, "Update Purchases", false),
                        new Action(4, Purchases, Delete, "Delete Purchases", false),
                        new Action(5, Purchases, Receive, "Receive Purchases", false)
                )),
                new Resource(9, Orders, List.of(
                        new Action(1, Orders, View, "View Orders", true),
                        new Action(2, Orders, Create, "Create Orders", false),
                        new Action(3, Orders, Update, "Update Orders", false),
                        new Action(4, Orders, Delete, "Delete Orders", false),
                        new Action(5, Orders, Receive, "Receive Orders", false)
                ))
        );
    }

}
