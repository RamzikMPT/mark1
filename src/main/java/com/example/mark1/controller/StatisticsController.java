package com.example.mark1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@Controller
public class StatisticsController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/statistics")
    public String showStatistics(Model model) {
        // Карты для хранения статистики
        Map<String, Integer> orderStatusCounts = new HashMap<>();
        Map<String, Integer> productStockCounts = new HashMap<>();

        try (Connection connection = dataSource.getConnection()) {
            // Запрос для получения статистики по статусам заказов
            String queryOrders = "SELECT status, COUNT(*) AS count FROM orders GROUP BY status";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(queryOrders)) {
                while (rs.next()) {
                    orderStatusCounts.put(rs.getString("status"), rs.getInt("count"));
                }
            }

            // Запрос для получения остатков по товарам
            String queryProducts = "SELECT product_name, stock FROM products";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(queryProducts)) {
                while (rs.next()) {
                    productStockCounts.put(rs.getString("product_name"), rs.getInt("stock"));
                }
            }
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка загрузки статистики: " + e.getMessage());
        }

        // Добавляем данные в модель
        model.addAttribute("orderStatusCounts", orderStatusCounts);
        model.addAttribute("productStockCounts", productStockCounts);

        return "statistics";
    }
}
