package com.jcanseco.inventoryapi.dashboard;

import com.jcanseco.inventoryapi.shared.testing.TestcontainersConfiguration;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DashboardControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(authorities = {"Permissions.Dashboard.View"})
    @Sql("/dashboard-data.sql")
    @Test
    public void getPurchasesValueSummaryByPeriodStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/dashboard/purchases-value/summary/by-period")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("startDate", "2024-01-01T00:00:00")
                                .param("endDate", "2024-02-01T00:00:00")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalValue").value(580));
    }

    @WithMockUser(authorities = {"Permissions.Dashboard.View"})
    @Sql("/dashboard-data.sql")
    @Test
    public void getProductsWithLowStockCountStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/dashboard/products/low-stock/count")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("stockThreshold", "5")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(4));
    }

    @WithMockUser(authorities = {"Permissions.Dashboard.View"})
    @Sql("/dashboard-data.sql")
    @Test
    public void getOutOfStockProductsCountStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/dashboard/products/out-of-stock/count")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));
    }

    @WithMockUser(authorities = {"Permissions.Dashboard.View"})
    @Sql("/dashboard-data.sql")
    @Test
    public void getProductsWithLowStockStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/dashboard/products/low-stock")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("stockThreshold", "5")
                                .param("limit", "2")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(5))
                .andExpect(jsonPath("$[0].name").value("Keyboard"))
                .andExpect(jsonPath("$[0].stockQuantity").value(0))
                .andExpect(jsonPath("$[1].id").value(4))
                .andExpect(jsonPath("$[1].name").value("Chair"))
                .andExpect(jsonPath("$[1].stockQuantity").value(1));
    }

    @WithMockUser(authorities = {"Permissions.Dashboard.View"})
    @Sql("/dashboard-data.sql")
    @Test
    public void getSalesValueSummaryByPeriodStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/dashboard/sales-value/summary/by-period")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("startDate", "2024-01-01T00:00:00")
                                .param("endDate", "2024-02-01T00:00:00")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalValue").value(1110));
    }

    @WithMockUser(authorities = {"Permissions.Dashboard.View"})
    @Sql("/dashboard-data.sql")
    @Test
    public void getMonthlySalesSeriesStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/dashboard/sales-value/series/monthly")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("startDate", "2024-01-01T00:00:00")
                                .param("endDate", "2024-04-01T00:00:00")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].year").value(2024))
                .andExpect(jsonPath("$[0].month").value(1))
                .andExpect(jsonPath("$[0].totalValue").value(1110))
                .andExpect(jsonPath("$[1].year").value(2024))
                .andExpect(jsonPath("$[1].month").value(2))
                .andExpect(jsonPath("$[1].totalValue").value(480))
                .andExpect(jsonPath("$[2].year").value(2024))
                .andExpect(jsonPath("$[2].month").value(3))
                .andExpect(jsonPath("$[2].totalValue").value(0));
    }

    @WithMockUser(authorities = {"Permissions.Dashboard.View"})
    @Sql("/dashboard-data.sql")
    @Test
    public void getProductsCountByCategoryStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/dashboard/products/count/by-category")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].categoryId").value(1))
                .andExpect(jsonPath("$[0].categoryName").value("Electronics"))
                .andExpect(jsonPath("$[0].productCount").value(3))
                .andExpect(jsonPath("$[1].categoryId").value(2))
                .andExpect(jsonPath("$[1].categoryName").value("Furniture"))
                .andExpect(jsonPath("$[1].productCount").value(2));
    }

    @WithMockUser(authorities = {"Permissions.Dashboard.View"})
    @Sql("/dashboard-data.sql")
    @Test
    public void getTopCustomersByRevenueStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                                get("/api/dashboard/top-customers/by-revenue")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("startDate", "2024-01-01T00:00:00")
                                .param("endDate", "2024-02-01T00:00:00")
                                .param("limit", "2")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].fullName").value("Jane Smith"))
                .andExpect(jsonPath("$[0].totalRevenue").value(600))
                .andExpect(jsonPath("$[1].id").value(1))
                .andExpect(jsonPath("$[1].fullName").value("John Doe"))
                .andExpect(jsonPath("$[1].totalRevenue").value(510));
    }

    @WithMockUser(authorities = {"Permissions.Dashboard.View"})
    @Sql("/dashboard-data.sql")
    @Test
    public void getTopSoldProductsStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                                get("/api/dashboard/top-products/by-sales")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("startDate", "2024-01-01T00:00:00")
                                .param("endDate", "2024-02-01T00:00:00")
                                .param("limit", "2")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productId").value(3))
                .andExpect(jsonPath("$[0].productName").value("Desk"))
                .andExpect(jsonPath("$[0].totalSold").value(5))
                .andExpect(jsonPath("$[1].productId").value(2))
                .andExpect(jsonPath("$[1].productName").value("Mouse"))
                .andExpect(jsonPath("$[1].totalSold").value(3));
    }

    @WithMockUser(authorities = {"Permissions.Dashboard.View"})
    @Sql("/dashboard-data.sql")
    @Test
    public void getTopSuppliersByRevenueStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                                get("/api/dashboard/top-suppliers/by-revenue")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("startDate", "2024-01-01T00:00:00")
                                .param("endDate", "2024-02-01T00:00:00")
                                .param("limit", "2")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("ABC Corp"))
                .andExpect(jsonPath("$[0].totalRevenue").value(300))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Tech Solutions Inc"))
                .andExpect(jsonPath("$[1].totalRevenue").value(280));
    }

    @WithMockUser(authorities = {"Permissions.Dashboard.View"})
    @Sql("/dashboard-data.sql")
    @Test
    public void getTotalInventoryValueSummaryStatusShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/dashboard/inventory-value/summary/total")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalValue").value(1300))
                .andExpect(jsonPath("$", Matchers.notNullValue()));
    }
}
