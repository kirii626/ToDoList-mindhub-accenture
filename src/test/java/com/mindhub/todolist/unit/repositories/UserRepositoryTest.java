package com.mindhub.todolist.unit.repositories;

import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.models.enums.RoleName;
import com.mindhub.todolist.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        when(passwordEncoder.encode(org.mockito.ArgumentMatchers.anyString()))
                .thenReturn("mockedPassword");

        org.mockito.Mockito.when(passwordEncoder.encode(org.mockito.ArgumentMatchers.anyString()))
                .thenReturn("mockedPassword");

        UserEntity user = new UserEntity(
                "testuser",
                "test@example.com",
                RoleName.USER,
                passwordEncoder.encode("12345")
        );

        userRepository.save(user);
    }

    @Test
    @Transactional
    void testFindByEmailUserExists() {
        Optional<UserEntity> result = userRepository.findByEmail("test@example.com");
        assertThat(result).isPresent();
    }

    @Test
    void testFindByEmailUserDoesNotExist() {
        Optional<UserEntity> result = userRepository.findByEmail("nonexistent@example.com");
        assertThat(result).isNotPresent();
    }

    @Test
    void testExistsByUsernameUserExists() {
        boolean exists = userRepository.existsByUsername("testuser");
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByUsername_UserDoesNotExist() {
        boolean exists = userRepository.existsByUsername("unknownuser");
        assertThat(exists).isFalse();
    }

    @Test
    void testCountByEmail_UserExists() {
        long count = userRepository.countByEmail("test@example.com");
        assertThat(count).isEqualTo(1);
    }

    @Test
    void testCountByEmail_UserDoesNotExist() {
        long count = userRepository.countByEmail("nonexistent@example.com");
        assertThat(count).isZero();
    }
}
