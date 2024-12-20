package com.example.mark1.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders") // Задаем имя таблицы
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    @Column(nullable = false)
    private String status = "PENDING";

    private String productName;
    private double price;
    private int quantity;
    private Long userId = 2L;
    private LocalDateTime orderDate = LocalDateTime.now();
}
