package com.example.mark1.controller;

import com.example.mark1.model.Courier;
import com.example.mark1.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/kuri")
public class CourierController {

    @Autowired
    private CourierService courierService;

    @GetMapping
    public String showCouriers(@RequestParam(required = false) String query, Model model) {
        model.addAttribute("couriers", courierService.filterCouriers(query));
        model.addAttribute("query", query); // Для отображения текущего значения фильтра
        return "kuri";
    }

    @PostMapping("/add")
    public String addCourier(@RequestParam String name, @RequestParam String phone) {
        courierService.addCourier(new Courier(name, phone));
        return "redirect:/kuri";
    }

    @GetMapping("/delete/{id}")
    public String deleteCourier(@PathVariable("id") Long id) {
        courierService.deleteCourier(id);
        return "redirect:/kuri";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("courier", courierService.getCourierById(id));
        return "editCourier";
    }

    @PostMapping("/edit/{id}")
    public String editCourier(@PathVariable("id") Long id, @RequestParam String name, @RequestParam String phone) {
        courierService.updateCourier(id, name, phone);
        return "redirect:/kuri";
    }
}
