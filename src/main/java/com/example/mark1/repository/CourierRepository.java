package com.example.mark1.repository;

import com.example.mark1.model.Courier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourierRepository extends JpaRepository<Courier, Long> {
    List<Courier> findByNameContainingIgnoreCaseOrPhoneContainingIgnoreCase(String
                                                                                    name, String phone);
}
