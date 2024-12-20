package com.example.mark1.controller;

import com.example.mark1.model.User;
import com.example.mark1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // Отображение формы регистрации
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // Страница регистрации
    }

    // Обработка регистрации
    @PostMapping("/register")
    public String registerUser(User user, Model model) {
        try {
            userService.registerUser(user);
            return "redirect:/login"; // Перенаправление на страницу логина после успешной регистрации
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при регистрации. Возможно, имя пользователя уже занято.");
            return "register"; // Возврат на страницу регистрации при ошибке
        }
    }

    // Отображение формы логина
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Страница логина
    }

    // Обработка авторизации
    @PostMapping("/login")
    public String loginUser(String username, String password, Model model) {
        User user = userService.authenticateUser(username, password);
        if (user != null) {
            // Проверка на конкретного пользователя adminBD
            if ("adminBD".equals(user.getUsername())) {
                return "redirect:/adminbdpage"; // Перенаправление на страницу администратора БД
            }

            // Проверка ролей пользователя
            switch (user.getRole().getRoleName()) {
                case "Админ":
                    return "redirect:/adminpage"; // Перенаправление на страницу администратора
                case "Пользователь":
                    return "redirect:/userpage"; // Перенаправление на страницу пользователя
                case "Продавец":
                    return "redirect:/sellerpage"; // Перенаправление на страницу продавца
                default:
                    break;
            }
        }

        // Если логин или пароль неверные
        model.addAttribute("error", "Неверный логин или пароль");
        return "login"; // Возврат к форме логина
    }

    // Страница для администратора
    @GetMapping("/adminpage")
    public String adminPage() {
        return "adminpage"; // Убедитесь, что файл adminpage.html существует в папке templates
    }

    // Страница для продавца
    @GetMapping("/sellerpage")
    public String sellerPage() {
        return "sellerpage"; // Убедитесь, что файл sellerpage.html существует в папке templates
    }

    // Страница для пользователя
    @GetMapping("/userpage")
    public String userPage() {
        return "userpage"; // Убедитесь, что файл userpage.html существует в папке templates
    }

    // Страница для администратора БД
    @GetMapping("/adminbdpage")
    public String adminBDPage() {
        return "adminbdpage"; // Убедитесь, что файл adminbdpage.html существует в папке templates
    }
}
