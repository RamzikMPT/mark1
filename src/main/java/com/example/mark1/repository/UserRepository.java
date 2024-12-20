package com.example.mark1.repository;

import com.example.mark1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing {@link User} entities.
 * Provides methods for retrieving users based on username and role criteria.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for
     * @return the user with the given username, or {@code null} if none found
     */
    User findByUsername(String username);

    /**
     * Finds users whose usernames contain the given string, ignoring case.
     *
     * @param username the string to search for within usernames
     * @return a list of users with matching usernames
     */
    List<User> findByUsernameContainingIgnoreCase(String username);

    /**
     * Finds users by their role ID.
     *
     * @param roleId the ID of the role to search for
     * @return a list of users with the specified role ID
     */
    List<User> findByRoleRoleId(Long roleId);

    /**
     * Finds users whose usernames contain the given string (case-insensitive)
     * and have the specified role ID.
     *
     * @param username the string to search for within usernames
     * @param roleId   the ID of the role to filter by
     * @return a list of users matching the given criteria
     */
    List<User> findByUsernameContainingIgnoreCaseAndRoleRoleId(String username, Long roleId);
}
