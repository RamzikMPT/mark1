package com.example.mark1.controller;

import com.example.mark1.model.Product;
import com.example.mark1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserCont2 {

    private final ProductService productService;

    @Autowired
    public UserCont2(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String showProducts(Model model, @RequestParam(value = "query", required = false) String query) {
        // Используем Optional для более компактной логики фильтрации
        List<Product> products = Optional.ofNullable(query)
                .filter(q -> !q.isEmpty())
                .map(productService::filterProducts)
                .orElseGet(productService::getAllProducts);

        // Добавляем список товаров и текущий фильтр в модель
        model.addAttribute("products", products);
        model.addAttribute("query", query); // Для отображения текущего фильтра

        // Возвращаем шаблон для отображения товаров
        return "user_products";
    }
}
