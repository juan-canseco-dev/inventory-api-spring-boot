package com.jcanseco.inventoryapi.identity.permissions.usecases.getall;

import com.jcanseco.inventoryapi.identity.permissions.domain.PermissionCatalog;
import com.jcanseco.inventoryapi.identity.permissions.domain.Resource;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetResourcesUseCase {

    private final PermissionCatalog permissionCatalog;

    public List<Resource> execute() {
        return permissionCatalog.getAllResources();
    }
}

