package com.khangpham.store.controllers;

import com.khangpham.store.dtos.AddItemToCartRequest;
import com.khangpham.store.dtos.CartDto;
import com.khangpham.store.dtos.CartItemDto;
import com.khangpham.store.dtos.UpdateCartItemRequest;
import com.khangpham.store.exceptions.CartNotFoundException;
import com.khangpham.store.exceptions.ProductNotFoundException;
import com.khangpham.store.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
@Tag(name = "Carts")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriBuilder
    ) {
        CartDto response = cartService.createCart();

        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PostMapping("/{cartId}/items")
    @Operation(summary = "Add item to a cart")
    public ResponseEntity<CartItemDto> addProduct(
            @Parameter(description = "The Id of the cart")
            @PathVariable(name = "cartId") UUID cartId,
            @RequestBody AddItemToCartRequest request
    ) {
        var cartItemDto = cartService.addProduct(cartId, request.getProductID());

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(
            @PathVariable(name = "cartId") UUID cartId
    ) {
       var cartDto = cartService.getCart(cartId);

        return ResponseEntity.ok(cartDto);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateItem (
            @PathVariable(name = "cartId") UUID cartId,
            @PathVariable(name = "productId") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
            ) {
        var cartItemDto = cartService.updateItem(cartId, productId, request.getQuantity());
        return ResponseEntity.ok(cartItemDto);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeItem(
            @PathVariable(name = "cartId") UUID cartId,
            @PathVariable(name = "productId") Long productId
    ) {
        cartService.removeItem(cartId, productId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{cartId}/items")
    public ResponseEntity<?> clearCart(
            @PathVariable(name = "cartId") UUID cartId
    ) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("Error", "Cart not found")
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("Error", "Product not found in the cart")
        );
    }
}
