package com.khangpham.store.mappers;

import com.khangpham.store.dtos.CartDto;
import com.khangpham.store.dtos.CartItemDto;
import com.khangpham.store.entities.Cart;
import com.khangpham.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "items", source = "cartItems")
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
}
