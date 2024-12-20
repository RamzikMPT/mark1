package com.example.mark1.controller;

import com.example.mark1.model.Role;
import com.example.mark1.model.User;
import com.example.mark1.service.RoleService;
import com.example.mark1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ustables")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String showUsers(@RequestParam(required = false) String search,
                            @RequestParam(required = false) Long roleId,
                            Model model) {
        List<User> users;
        List<Role> roles = roleService.getAllRoles();

        // Логика фильтрации
        if (search != null && !search.isEmpty() && roleId != null) {
            users = userService.findUsersByUsernameAndRole(search, roleId);
        } else if (search != null && !search.isEmpty()) {
            users = userService.findUsersByUsername(search);
        } else if (roleId != null) {
            users = userService.findUsersByRole(roleId);
        } else {
            users = userService.getAllUsers();
        }

        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        model.addAttribute("search", search);
        model.addAttribute("selectedRoleId", roleId);
        return "ustables";
    }

    @PostMapping("/add")
    public String addUser(@RequestParam String username, @RequestParam String email,
                          @RequestParam String password, @RequestParam Long roleId) {
        userService.addUser(username, email, password, roleId);
        return "redirect:/ustables";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/ustables";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "editUser"; // Создадим editUser.html для редактирования
    }

    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id,
                           @RequestParam String username,
                           @RequestParam String email,
                           @RequestParam Long roleId) {
        userService.updateUser(id, username, email, roleId);
        return "redirect:/ustables";
    }
}
