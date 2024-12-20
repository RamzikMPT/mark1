package com.example.mark1.repository;

import com.example.mark1.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(Long userId); // Метод для поиска заказов по ID пользователя
}
