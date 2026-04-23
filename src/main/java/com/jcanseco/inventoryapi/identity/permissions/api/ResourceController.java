package com.jcanseco.inventoryapi.identity.permissions.api;

import com.jcanseco.inventoryapi.identity.permissions.domain.Resource;
import com.jcanseco.inventoryapi.identity.permissions.usecases.getall.GetResourcesUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequestMapping("api/resources")
@RestController
@RequiredArgsConstructor
public class ResourceController {

    private final GetResourcesUseCase getResourcesUseCase;

    @GetMapping
    public ResponseEntity<List<Resource>> getResources() {
        return ResponseEntity.ok(getResourcesUseCase.execute());
    }
}
