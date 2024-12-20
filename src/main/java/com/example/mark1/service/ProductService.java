package com.example.mark1.service;

import com.example.mark1.model.Product;
import java.util.List;

public interface ProductService {
    Product saveProduct(Product product);
    void deleteProduct(Long id);
    List<Product> getAllProducts();
    Product getProductById(Long id);

    // Метод для фильтрации продуктов
    List<Product> filterProducts(String query);
}
