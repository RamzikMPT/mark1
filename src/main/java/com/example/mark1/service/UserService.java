package com.example.mark1.service;

import com.example.mark1.model.Role;
import com.example.mark1.model.User;
import com.example.mark1.repository.RoleRepository;
import com.example.mark1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User registerUser(User user) {
        Role userRole = roleRepository.findByRoleName("Пользователь");

        // Хэшируем пароль перед сохранением
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        user.setRole(userRole);

        return userRepository.save(user);
    }

    public User authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user != null) {
            // Если пароль не захеширован, проверяем его напрямую
            if (user.getPassword().equals(password)) {
                // Хэшируем пароль и обновляем в базе
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                user.setPassword(hashedPassword);
                userRepository.save(user);
                return user;
            }

            // Если пароль хеширован, проверяем с помощью BCrypt
            if (BCrypt.checkpw(password, user.getPassword())) {
                return user;
            }
        }

        return null; // Если пользователь не найден или пароль неверный
    }

    public void addUser(String username, String email, String password, Long roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);

        // Хэшируем пароль перед сохранением
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(username, email, hashedPassword, role);

        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void updateUser(Long id, String username, String email, Long roleId) {
        User user = getUserById(id);
        if (user != null) {
            user.setUsername(username);
            user.setEmail(email);

            Role role = roleRepository.findById(roleId).orElse(null);
            user.setRole(role);

            userRepository.save(user);
        }
    }

    public List<User> findUsersByUsername(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }

    public List<User> findUsersByRole(Long roleId) {
        return userRepository.findByRoleRoleId(roleId);
    }

    public List<User> findUsersByUsernameAndRole(String username, Long roleId) {
        return userRepository.findByUsernameContainingIgnoreCaseAndRoleRoleId(username, roleId);
    }
}