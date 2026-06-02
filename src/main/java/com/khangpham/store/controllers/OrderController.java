package com.khangpham.store.controllers;

import com.khangpham.store.dtos.ErrorDto;
import com.khangpham.store.dtos.OrderDto;
import com.khangpham.store.exceptions.OrderNotFoundException;
import com.khangpham.store.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getALlOrders());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(
            @PathVariable(name = "orderId") Long orderId
    ) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorDto> handleExceptionOrderNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorDto(ex.getMessage())
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleExceptionAccessDenied(Exception ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorDto(ex.getMessage())
        );
    }

}
