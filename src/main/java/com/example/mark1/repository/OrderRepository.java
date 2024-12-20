package com.example.mark1.repository;

import com.example.mark1.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing {@link Order} entities.
 * Provides methods for retrieving orders based on user criteria.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Finds all orders associated with a specific user by their user ID.
     *
     * @param userId the ID of the user whose orders are to be retrieved
     * @return a list of orders belonging to the specified user
     */
    List<Order> findAllByUserId(Long userId);
}
