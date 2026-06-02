package com.khangpham.store.mappers;

import com.khangpham.store.dtos.OrderDto;
import com.khangpham.store.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
