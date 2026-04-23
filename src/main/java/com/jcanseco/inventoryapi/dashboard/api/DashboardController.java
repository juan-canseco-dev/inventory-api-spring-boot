package com.jcanseco.inventoryapi.dashboard.api;

import com.jcanseco.inventoryapi.dashboard.dto.GetProductsWithLowStockCountRequest;
import com.jcanseco.inventoryapi.dashboard.dto.GetProductsWithLowStockRequest;
import com.jcanseco.inventoryapi.dashboard.dto.GetTopCustomersByRevenueRequest;
import com.jcanseco.inventoryapi.dashboard.dto.GetTopSoldProductsRequest;
import com.jcanseco.inventoryapi.dashboard.dto.GetTopSuppliersByRevenueRequest;
import com.jcanseco.inventoryapi.dashboard.dto.GetValueSummaryRequest;
import com.jcanseco.inventoryapi.dashboard.usecases.GetInventoryValueSummaryByPeriodUseCase;
import com.jcanseco.inventoryapi.dashboard.usecases.GetProductsWithLowStockCountUseCase;
import com.jcanseco.inventoryapi.dashboard.usecases.GetProductsWithLowStockUseCase;
import com.jcanseco.inventoryapi.dashboard.usecases.GetSalesValueSummaryByPeriodUseCase;
import com.jcanseco.inventoryapi.dashboard.usecases.GetTopCustomersByRevenueUseCase;
import com.jcanseco.inventoryapi.dashboard.usecases.GetTopSoldProductsUseCase;
import com.jcanseco.inventoryapi.dashboard.usecases.GetTopSuppliersByRevenueUseCase;
import com.jcanseco.inventoryapi.dashboard.usecases.GetTotalInventoryValueSummaryUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@PreAuthorize("hasAuthority(@Permissions.permissionOf(@Resource.Dashboard, @Action.View))")
@Validated
@RestControllerAdvice
@RequestMapping("api/dashboard")
@RestController
@RequiredArgsConstructor
public class DashboardController {

    private final GetInventoryValueSummaryByPeriodUseCase getInventoryValueSummaryByPeriodUseCase;
    private final GetProductsWithLowStockCountUseCase getProductsWithLowStockCountUseCase;
    private final GetProductsWithLowStockUseCase getProductsWithLowStockUseCase;
    private final GetSalesValueSummaryByPeriodUseCase getSalesValueSummaryByPeriodUseCase;
    private final GetTopCustomersByRevenueUseCase getTopCustomersByRevenueUseCase;
    private final GetTopSoldProductsUseCase getTopSoldProductsUseCase;
    private final GetTopSuppliersByRevenueUseCase getTopSuppliersByRevenueUseCase;
    private final GetTotalInventoryValueSummaryUseCase getTotalInventoryValueSummaryUseCase;

    @GetMapping("inventory-value/summary/by-period")
    public ResponseEntity<?> getInventoryValueSummaryByPeriod(@Valid GetValueSummaryRequest request) {
        return ResponseEntity.ok(getInventoryValueSummaryByPeriodUseCase.execute(request));
    }

    @GetMapping("products/low-stock/count")
    public ResponseEntity<?> getProductsWithLowStockCount(@Valid GetProductsWithLowStockCountRequest request) {
        return ResponseEntity.ok(getProductsWithLowStockCountUseCase.execute(request));
    }

    @GetMapping("products/low-stock")
    public ResponseEntity<?> getProductsWithLowStock(@Valid GetProductsWithLowStockRequest request) {
        return ResponseEntity.ok(getProductsWithLowStockUseCase.execute(request));
    }

    @GetMapping("sales-value/summary/by-period")
    public ResponseEntity<?> getSalesValueSummaryByPeriod(@Valid GetValueSummaryRequest request) {
        return ResponseEntity.ok(getSalesValueSummaryByPeriodUseCase.execute(request));
    }

    @GetMapping("top-customers/by-revenue")
    public ResponseEntity<?> getTopCustomersByRevenue(@Valid GetTopCustomersByRevenueRequest request) {
        return ResponseEntity.ok(getTopCustomersByRevenueUseCase.execute(request));
    }

    @GetMapping("top-products/by-sales")
    public ResponseEntity<?> getTopSoldProducts(@Valid GetTopSoldProductsRequest request) {
        return ResponseEntity.ok(getTopSoldProductsUseCase.execute(request));
    }

    @GetMapping("top-suppliers/by-revenue")
    public ResponseEntity<?> getTopSuppliersByRevenue(@Valid GetTopSuppliersByRevenueRequest request) {
        return ResponseEntity.ok(getTopSuppliersByRevenueUseCase.execute(request));
    }

    @GetMapping("inventory-value/summary/total")
    public ResponseEntity<?> getTotalInventoryValueSummary() {
        return ResponseEntity.ok(getTotalInventoryValueSummaryUseCase.execute());
    }
}
