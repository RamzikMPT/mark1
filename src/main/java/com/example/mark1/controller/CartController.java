package com.example.mark1.controller;

import com.example.mark1.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;


    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam(defaultValue = "1") int quantity) {
        cartService.addProductToCart(productId, quantity); // Убрали userId
        return "redirect:/cart";
    }

    @GetMapping
    public String viewCart(Model model) {
        // Здесь вам нужно передать userId, если хотите показывать корзину для конкретного пользователя
        model.addAttribute("cartItems", cartService.getCartItems(2L)); // Можно также использовать константу
        return "cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long cartId) {
        cartService.removeProductFromCart(cartId);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long cartId, @RequestParam int quantity) {
        cartService.updateProductQuantity(cartId, quantity);
        return "redirect:/cart";
    }


}
