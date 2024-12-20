package com.example.mark1.repository;

import com.example.mark1.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(Long userId); // Метод для поиска по ID пользователя
}