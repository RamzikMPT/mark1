package com.example.mark1.service;

import com.example.mark1.model.Shipment;
import com.example.mark1.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    // Получить все отправки
    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    // Найти отправку по ID
    public Shipment getShipmentById(Long id) {
        return shipmentRepository.findById(id).orElse(null);
    }

    // Сохранить новую отправку
    public Shipment saveShipment(Shipment shipment) {
        return shipmentRepository.save(shipment);
    }

    // Обновить существующую отправку
    public Shipment updateShipment(Long id, Shipment shipmentDetails) {
        Shipment shipment = getShipmentById(id);
        if (shipment != null) {
            shipment.setOrderId(shipmentDetails.getOrderId());
            shipment.setStatus(shipmentDetails.getStatus());
            return shipmentRepository.save(shipment);
        }
        return null;
    }

    // Удалить отправку по ID
    public void deleteShipment(Long id) {
        shipmentRepository.deleteById(id);
    }
}

