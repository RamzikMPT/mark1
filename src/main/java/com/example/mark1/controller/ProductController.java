package com.example.mark1.controller;

import com.example.mark1.model.Product;
import com.example.mark1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/seller")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/cards")
    public String showProductForm(@RequestParam(required = false) String query, Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("products", productService.filterProducts(query));
        model.addAttribute("query", query);
        return "product_cards";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute("product") Product product) {
        productService.saveProduct(product);
        return "redirect:/seller/cards";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product_cards";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/seller/cards";
    }
}
