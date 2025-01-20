package com.mindhub.todolist.integration.services;

import com.mindhub.todolist.config.JwtAuthenticationFilter;
import com.mindhub.todolist.config.JwtUtils;
import com.mindhub.todolist.dtos.NewUserDto;
import com.mindhub.todolist.dtos.UserDto;
import com.mindhub.todolist.exceptions.UserAlreadyExistsExc;
import com.mindhub.todolist.exceptions.UserNotFoundExc;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.models.enums.RoleName;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        testUser = new UserEntity("testuser", "test@example.com", RoleName.USER, "securePassword123");
        userRepository.save(testUser);
    }

    @Test
    void testGetUserEntityByIdSuccess() {
        UserDto result = userService.getUserEntityById(testUser.getId());

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
    }

    @Test
    void testGetUserEntityByIdNotFound() {
        assertThatThrownBy(() -> userService.getUserEntityById(999L))
                .isInstanceOf(UserNotFoundExc.class)
                .hasMessageContaining("User not found by ID: 999");
    }

    @Test
    void testCreateUserEntitySuccess() {
        NewUserDto newUserDto = new NewUserDto("newuser", "new@example.com", "password123");

        UserDto createdUser = userService.createUserEntity(newUserDto);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getUsername()).isEqualTo("newuser");
        assertThat(userRepository.findByEmail("new@example.com")).isPresent();
    }

    @Test
    void testCreateUserEntityEmailAlreadyExists() {
        NewUserDto duplicateUser = new NewUserDto("testuser", "test@example.com", "password123");

        assertThatThrownBy(() -> userService.createUserEntity(duplicateUser))
                .isInstanceOf(UserAlreadyExistsExc.class)
                .hasMessageContaining("The email already exists: test@example.com");
    }

    @Test
    void testDeleteUserEntitySuccess() {
        userService.deleteUserEntity(testUser.getId());

        assertThat(userRepository.findById(testUser.getId())).isNotPresent();
    }

    @Test
    void testDeleteUserEntityNotFound() {
        assertThatThrownBy(() -> userService.deleteUserEntity(999L))
                .isInstanceOf(UserNotFoundExc.class)
                .hasMessageContaining("User not found by ID: 999");
    }

    @Test
    void testAssignRoleToUserSuccess() {
        UserDto updatedUser = userService.assignRoleToUser(testUser.getId(), RoleName.ADMIN);

        assertThat(updatedUser.getRoleName()).isEqualTo(RoleName.ADMIN);
    }

    @Test
    void testAssignRoleToUserNotFound() {
        assertThatThrownBy(() -> userService.assignRoleToUser(999L, RoleName.ADMIN))
                .isInstanceOf(UserNotFoundExc.class)
                .hasMessageContaining("User not found by ID: 999");
    }
}
