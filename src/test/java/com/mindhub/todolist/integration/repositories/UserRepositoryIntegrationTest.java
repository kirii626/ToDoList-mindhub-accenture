package com.mindhub.todolist.integration.repositories;

import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.models.enums.RoleName;
import com.mindhub.todolist.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    private UserEntity user1;
    private UserEntity user2;

    @BeforeEach
    void setUp() {
        // Initialize and save sample users
        user1 = new UserEntity();
        user1.setUsername("testuser1");
        user1.setEmail("testuser1@example.com");
        user1.setPassword("password123");
        user1.setRoleName(RoleName.USER);
        userRepository.save(user1);

        user2 = new UserEntity();
        user2.setUsername("testuser2");
        user2.setEmail("testuser2@example.com");
        user2.setPassword("password456");
        user2.setRoleName(RoleName.ADMIN);
        userRepository.save(user2);
    }

    @Test
    void findByEmailShouldReturnUserWhenEmailExists() {
        Optional<UserEntity> foundUser = userRepository.findByEmail("testuser1@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals("testuser1", foundUser.get().getUsername());
        assertEquals(RoleName.USER, foundUser.get().getRoleName());
    }

    @Test
    void findByEmailShouldReturnEmptyWhenEmailDoesNotExist() {
        Optional<UserEntity> foundUser = userRepository.findByEmail("nonexistent@example.com");
        assertFalse(foundUser.isPresent());
    }

    @Test
    void existsByUsernameShouldReturnTrueWhenUsernameExists() {
        boolean exists = userRepository.existsByUsername("testuser1");
        assertTrue(exists);
    }

    @Test
    void existsByUsernameShouldReturnFalseWhenUsernameDoesNotExist() {
        boolean exists = userRepository.existsByUsername("nonexistentuser");
        assertFalse(exists);
    }

    @Test
    void countByEmailShouldReturnCorrectCountForEmail() {
        long count = userRepository.countByEmail("testuser1@example.com");
        assertEquals(1, count);

        count = userRepository.countByEmail("nonexistent@example.com");
        assertEquals(0, count);
    }

    @Test
    void deleteByEmailShouldDeleteUserWhenEmailExists() {
        userRepository.deleteByEmail("testuser1@example.com");
        Optional<UserEntity> deletedUser = userRepository.findByEmail("testuser1@example.com");
        assertFalse(deletedUser.isPresent());
    }

    @Test
    void deleteByEmailShouldNotThrowWhenEmailDoesNotExist() {
        assertDoesNotThrow(() -> userRepository.deleteByEmail("nonexistent@example.com"));
    }
}
