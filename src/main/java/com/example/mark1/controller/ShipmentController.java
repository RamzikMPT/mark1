package com.example.mark1.controller;

import com.example.mark1.model.Shipment;
import com.example.mark1.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/seller/shipments")
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    // Отображение всех отправок
    @GetMapping
    public String listShipments(Model model) {
        List<Shipment> shipments = shipmentService.getAllShipments();
        model.addAttribute("shipments", shipments);
        return "shipments"; // отображение страницы всех отправок
    }

    // Форма для добавления новой отправки
    @GetMapping("/new")
    public String showAddShipmentForm(Model model) {
        model.addAttribute("shipment", new Shipment());
        return "add-shipment"; // страница формы для добавления отправки
    }

    // Создание новой отправки
    @PostMapping("/create")
    public String createShipment(@ModelAttribute Shipment shipment) {
        shipmentService.saveShipment(shipment);
        return "redirect:/seller/shipments";
    }

    // Форма редактирования отправки
    @GetMapping("/edit/{id}")
    public String showEditShipmentForm(@PathVariable("id") Long id, Model model) {
        Shipment shipment = shipmentService.getShipmentById(id);
        model.addAttribute("shipment", shipment);
        return "edit-shipment"; // страница формы редактирования отправки
    }

    // Обновление отправки
    @PostMapping("/update/{id}")
    public String updateShipment(@PathVariable("id") Long id, @ModelAttribute Shipment shipment) {
        shipmentService.updateShipment(id, shipment);
        return "redirect:/seller/shipments";
    }

    // Удаление отправки
    @GetMapping("/delete/{id}")
    public String deleteShipment(@PathVariable("id") Long id) {
        shipmentService.deleteShipment(id);
        return "redirect:/seller/shipments";
    }
}
