package com.example.mark1.controller;

import com.example.mark1.model.Order;
import com.example.mark1.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/ord")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    // Просмотр всех заказов
    @GetMapping
    public String showOrders(Model model) {
        List<Order> orders = orderRepository.findAllByUserId(2L);
        model.addAttribute("orders", orders);
        return "ord";
    }

    // Создание нового заказа
    @PostMapping("/create")
    public String createOrder(
            @RequestParam String productName,
            @RequestParam double price,
            @RequestParam int quantity) {
        Order order = new Order();
        order.setProductName(productName);
        order.setPrice(price);
        order.setQuantity(quantity);
        order.setStatus("PENDING");
        order.setOrderDate(LocalDateTime.now()); // Устанавливаем текущую дату и время
        orderRepository.save(order);
        return "redirect:/ord";
    }

    // Обновление заказа
    @PostMapping("/update")
    public String updateOrder(
            @RequestParam Long orderId,
            @RequestParam String productName,
            @RequestParam double price,
            @RequestParam int quantity) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setProductName(productName);
        order.setPrice(price);
        order.setQuantity(quantity);
        orderRepository.save(order);
        return "redirect:/ord";
    }

    // Удаление заказа
    @PostMapping("/delete")
    public String deleteOrder(@RequestParam Long orderId) {
        orderRepository.deleteById(orderId);
        return "redirect:/ord";
    }
}


