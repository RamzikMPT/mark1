package com.example.mark1.service;

import com.example.mark1.model.Role;
import com.example.mark1.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // Метод для получения всех ролей
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
        
    }

    // Метод для поиска роли по имени
    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
