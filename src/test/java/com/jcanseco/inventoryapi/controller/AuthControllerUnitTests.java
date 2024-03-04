package com.jcanseco.inventoryapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.exceptions.NotFoundException;
import com.jcanseco.inventoryapi.security.controllers.AuthController;
import com.jcanseco.inventoryapi.security.controllers.UserController;
import com.jcanseco.inventoryapi.security.dtos.authentication.JwtAuthenticationDto;
import com.jcanseco.inventoryapi.security.dtos.authentication.SignInDto;
import com.jcanseco.inventoryapi.security.dtos.users.*;
import com.jcanseco.inventoryapi.security.services.*;
import com.jcanseco.inventoryapi.services.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBeans({
        @MockBean(CustomerService.class),
        @MockBean(ProductService.class),
        @MockBean(SupplierService.class),
        @MockBean(UnitService.class),
        @MockBean(PurchaseService.class),
        @MockBean(OrderService.class),
        @MockBean(CategoryService.class),
        @MockBean(ResourceService.class),
        @MockBean(RoleService.class),
        @MockBean(UserService.class),
        @MockBean(JwtService.class)
})
@WebMvcTest(
        controllers = AuthController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
public class AuthControllerUnitTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AuthenticationService service;

    @Test
    public void signInWhenModelIsValidStatusShouldBeOk() throws Exception {

        var dto = SignInDto.builder()
                .email("john.doe@mail.com")
                .password("password1234")
                .build();

        var jwtResponse = JwtAuthenticationDto.builder()
                .token("mockToken")
                .build();

        when(service.signIn(any())).thenReturn(jwtResponse);

        mockMvc.perform(
                        post("/api/auth/singIn")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(jwtResponse.getToken())));
    }

    @Test
    public void signInWhenModelIsInvalidStatusShouldBeBadRequest() throws Exception {

        var dto = SignInDto.builder()
                .email("john.doe@mail.com")
                .password("password1234")
                .build();

        when(service.signIn(any())).thenThrow(new BadCredentialsException("Bad Credentials"));


        mockMvc.perform(
                        post("/api/auth/singIn")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
