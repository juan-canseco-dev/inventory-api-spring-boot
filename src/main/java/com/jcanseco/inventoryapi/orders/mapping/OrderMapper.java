package com.jcanseco.inventoryapi.orders.mapping;

import com.jcanseco.inventoryapi.customers.domain.Customer;
import com.jcanseco.inventoryapi.orders.dto.OrderDetailsDto;
import com.jcanseco.inventoryapi.orders.dto.OrderDto;
import com.jcanseco.inventoryapi.orders.dto.OrderItemDto;
import com.jcanseco.inventoryapi.orders.domain.Order;
import com.jcanseco.inventoryapi.orders.domain.OrderItem;
import com.jcanseco.inventoryapi.shared.pagination.PagedList;
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






