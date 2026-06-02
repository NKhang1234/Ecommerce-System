package com.khangpham.store.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column (name = "unit_price")
    private BigDecimal unitPrice;

    @Column (name = "quantity", nullable = false)
    private Integer quantity;

    @Column (name = "total_price", nullable = false)
    private BigDecimal totalPrice;
}
