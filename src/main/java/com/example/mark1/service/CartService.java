package com.example.mark1.service;

import com.example.mark1.model.CartItem;
import com.example.mark1.model.Product;
import com.example.mark1.repository.CartItemRepository;
import com.example.mark1.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    private static final Long DEFAULT_USER_ID = 2L; // Константа для ID пользователя

    public void addProductToCart(Long productId, int quantity) {
        // Логика добавления товара в корзину
        CartItem cartItem = new CartItem();
        Product product = productRepository.findById(productId).orElseThrow();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        // Установка пользователя
        cartItem.setUserId(DEFAULT_USER_ID); // Установка фиксированного ID пользователя
        cartRepository.save(cartItem);
    }

    public List<CartItem> getCartItems(Long userId) {
        return cartRepository.findByUserId(userId); // Этот метод можно оставить как есть, если необходимо
    }

    public void removeProductFromCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    public void updateProductQuantity(Long cartId, int quantity) {
        CartItem cartItem = cartRepository.findById(cartId).orElseThrow();
        cartItem.setQuantity(quantity);
        cartRepository.save(cartItem);
    }
}
