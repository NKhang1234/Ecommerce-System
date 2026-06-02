package com.khangpham.store.repositories;

import com.khangpham.store.entities.Order;
import com.khangpham.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCustomer(User user);
    Order findByCustomer(User user);
}
