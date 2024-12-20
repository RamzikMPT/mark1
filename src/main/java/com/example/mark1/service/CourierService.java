package com.example.mark1.service;

import com.example.mark1.model.Courier;
import com.example.mark1.repository.CourierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourierService {

    @Autowired
    private CourierRepository courierRepository;

    public List<Courier> getAllCouriers() {
        return courierRepository.findAll();
    }

    public List<Courier> filterCouriers(String query) {
        if (query == null || query.isEmpty()) {
            return courierRepository.findAll();
        }
        return courierRepository.findByNameContainingIgnoreCaseOrPhoneContainingIgnoreCase(query, query);
    }

    public void addCourier(Courier courier) {
        courierRepository.save(courier);
    }

    public void deleteCourier(Long id) {
        courierRepository.deleteById(id);
    }

    public Courier getCourierById(Long id) {
        return courierRepository.findById(id).orElse(null);
    }

    public void updateCourier(Long id, String name, String phone) {
        Courier courier = getCourierById(id);
        if (courier != null) {
            courier.setName(name);
            courier.setPhone(phone);
            courierRepository.save(courier);
        }
    }
}
