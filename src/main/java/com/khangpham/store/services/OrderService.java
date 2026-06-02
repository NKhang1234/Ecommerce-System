package com.khangpham.store.services;

import com.khangpham.store.dtos.OrderDto;
import com.khangpham.store.exceptions.OrderNotFoundException;
import com.khangpham.store.mappers.OrderMapper;
import com.khangpham.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDto> getALlOrders() {
        var user = authService.getCurrentUser();
        var orders = orderRepository.findAllByCustomer(user);
        return orders.stream().map(orderMapper::toDto).toList();
    }

    public OrderDto getOrder(Long orderId) {
        var order = orderRepository.findById(orderId)
                                    .orElseThrow(OrderNotFoundException::new);

        var currentUser = authService.getCurrentUser();
        if (!order.isBelongTo(currentUser)) {
            throw new AccessDeniedException("The order does not belong to current user");
        }
        return orderMapper.toDto(order);
    }
}
