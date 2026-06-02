package com.khangpham.store.services;

import com.khangpham.store.dtos.CartDto;
import com.khangpham.store.dtos.CartItemDto;
import com.khangpham.store.entities.Cart;
import com.khangpham.store.exceptions.CartNotFoundException;
import com.khangpham.store.exceptions.ProductNotFoundException;
import com.khangpham.store.mappers.CartMapper;
import com.khangpham.store.repositories.CartRepository;
import com.khangpham.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public CartDto createCart() {
        var newCart = new Cart();
        cartRepository.save(newCart);
        CartDto response = cartMapper.toDto(newCart);
        response.setTotalPrice(new BigDecimal(0));

        return response;
    }

    public CartItemDto addProduct(UUID cartId, Long productId) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        var product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }

        var cartItem = cart.addItem(product);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public CartDto getCart(UUID cartId) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        return cartMapper.toDto(cart);
    }

    public CartItemDto updateItem(UUID cartId, Long productId, Integer quantity) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        var cartItem = cart.getItem(productId);

        if (cartItem == null) {
            throw new ProductNotFoundException();
        }
        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public void removeItem(UUID cartId, Long productId) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public void clearCart(UUID cartId) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        cart.clearCart();
        cartRepository.save(cart);
    }

}
