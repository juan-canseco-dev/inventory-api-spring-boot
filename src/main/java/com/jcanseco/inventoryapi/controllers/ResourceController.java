package com.jcanseco.inventoryapi.controllers;

import com.jcanseco.inventoryapi.security.resources.Resource;
import com.jcanseco.inventoryapi.security.services.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;

@RestControllerAdvice
@RequestMapping("api/resources")
@RestController
@RequiredArgsConstructor
public class ResourceController {
    private final ResourceService service;
    @GetMapping
    public ResponseEntity<List<Resource>> getResources() {
        return ResponseEntity.ok(
                service.getAll()
        );
    }
}
