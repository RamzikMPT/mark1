package com.example.mark1.repository;

import com.example.mark1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    List<User> findByUsernameContainingIgnoreCase(String username);

    List<User> findByRoleRoleId(Long roleId);

    List<User> findByUsernameContainingIgnoreCaseAndRoleRoleId(String
                                                                       username, Long roleId);
}
