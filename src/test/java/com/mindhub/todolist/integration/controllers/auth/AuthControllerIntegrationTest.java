package com.mindhub.todolist.integration.controllers.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhub.todolist.dtos.LoginUser;
import com.mindhub.todolist.dtos.NewUserDto;
import com.mindhub.todolist.exceptions.UserAlreadyExistsExc;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.models.enums.RoleName;
import com.mindhub.todolist.repositories.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        // Register DTO
        userRepository.deleteAll();

        UserEntity existingUser = new UserEntity();
        existingUser.setUsername("testuser");
        existingUser.setEmail("testuser@email.com");
        existingUser.setPassword(passwordEncoder.encode("password123")); // Usa la codificación real
        existingUser.setRoleName(RoleName.USER);
        userRepository.save(existingUser);
    }

    @AfterEach
    void teardown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        NewUserDto newUserDto = new NewUserDto("newuser", "newuser@email.com", "password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("newuser@email.com"));
    }

    @Test
    void shouldFailToRegisterWhenUserAlreadyExists() throws Exception {
        NewUserDto duplicateUserDto = new NewUserDto("testuser", "testuser@email.com", "password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateUserDto)))
                .andExpect(status().isConflict()) // 409 Conflict
                .andExpect(jsonPath("$.message").value("The email already exists: testuser@email.com"));
    }

    @Test
    void shouldAuthenticateUserSuccessfully() throws Exception {
        LoginUser loginUser = new LoginUser("testuser@email.com", "password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUser)))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.notNullValue()));
    }

    @Test
    void shouldFailToAuthenticateWithInvalidCredentials() throws Exception {
        LoginUser invalidLogin = new LoginUser("testuser@email.com", "wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidLogin)))
                .andExpect(status().isUnauthorized()) // 401 Unauthorized
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    @Test
    void shouldFailToRegisterWithInvalidData() throws Exception {
        NewUserDto invalidUserDto = new NewUserDto("", "invalidemail", "");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserDto)))
                .andExpect(status().isBadRequest()) // 400 Bad Request
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldFailToAuthenticateWithIncompleteData() throws Exception {
        LoginUser incompleteLogin = new LoginUser("", "");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incompleteLogin)))
                .andExpect(status().isBadRequest()) // 400 Bad Request
                .andExpect(jsonPath("$.message").exists());

    }
}
