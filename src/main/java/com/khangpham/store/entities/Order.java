package com.khangpham.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn (name = "customer_id", nullable = false)
    private User customer;

    @Column (name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column (name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column (name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @OneToMany (mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<OrderItem> items = new LinkedHashSet<>() ;

    public static Order createOrder(Cart cart, User customer) {
        var order = new Order() ;
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus(OrderStatus.PENDING);
        order.setCustomer(customer);

        cart.getCartItems().forEach(item -> {
            var orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setTotalPrice(item.getTotalPrice());
            orderItem.setUnitPrice(item.getProduct().getPrice());
            order.items.add(orderItem);
        });

        return order;
    }

    public boolean isBelongTo(User user) {
        return customer.equals(user);
    }

}
