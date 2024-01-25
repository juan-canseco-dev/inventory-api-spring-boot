package com.jcanseco.inventoryapi.mappers;

import com.jcanseco.inventoryapi.dtos.PagedList;
import com.jcanseco.inventoryapi.dtos.orders.OrderDetailsDto;
import com.jcanseco.inventoryapi.dtos.orders.OrderDto;
import com.jcanseco.inventoryapi.dtos.orders.OrderItemDto;
import com.jcanseco.inventoryapi.entities.Customer;
import com.jcanseco.inventoryapi.entities.Order;
import com.jcanseco.inventoryapi.entities.OrderItem;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto entityToDto(Order order);
    OrderDetailsDto entityToDetailsDto(Order order);
    default String customerToName(Customer customer) {
        return customer.getFullName();
    }
    OrderItemDto itemToDto(OrderItem item);

    default PagedList<OrderDto> pageToPagedList(Page<Order> page) {
        return new PagedList<>(
                page.get().map(this::entityToDto).toList(),
                page.getPageable().getPageNumber() + 1,
                page.getPageable().getPageSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }
}
