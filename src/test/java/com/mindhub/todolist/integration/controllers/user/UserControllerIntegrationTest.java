package com.mindhub.todolist.integration.controllers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhub.todolist.dtos.LoginUser;
import com.mindhub.todolist.dtos.NewUserDto;
import com.mindhub.todolist.dtos.UserDto;
import com.mindhub.todolist.exceptions.InvalidCredentialsExc;
import com.mindhub.todolist.exceptions.UserAlreadyExistsExc;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.models.enums.RoleName;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.AuthService;
import com.mindhub.todolist.services.UserService;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        // Pre-populate database with test data
        UserEntity existingUser = new UserEntity();
        existingUser.setUsername("existinguser");
        existingUser.setEmail("existinguser@email.com");
        existingUser.setPassword(passwordEncoder.encode("password123"));
        existingUser.setRoleName(RoleName.USER);
        userRepository.save(existingUser);
    }

    @AfterEach
    void teardown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        NewUserDto newUserDto = new NewUserDto("testuser", "testuser@email.com", "password123");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("testuser@email.com"))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void shouldFailToRegisterWhenUserAlreadyExists() throws Exception {
        NewUserDto newUserDto = new NewUserDto("existinguser", "existinguser@email.com", "password123");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserDto)))
                .andExpect(status().isConflict()) // 409 Conflict
                .andExpect(jsonPath("$.message").value("The email already exists: existinguser@email.com"));
    }

    @Test
    void shouldAuthenticateAndReturnJwt() throws Exception {
        LoginUser loginUser = new LoginUser("existinguser@email.com", "password123");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUser)))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.notNullValue())); // Ensure JWT is returned
    }

    @Test
    void shouldFailToAuthenticateWithInvalidCredentials() throws Exception {
        LoginUser invalidLogin = new LoginUser("wronguser@email.com", "wrongpassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidLogin)))
                .andExpect(status().isUnauthorized()) // 401 Unauthorized
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    @Test
    void shouldFailToRegisterWhenDataIsInvalid() throws Exception {
        NewUserDto invalidUserDto = new NewUserDto("w", "invalidgmail.com", "Password12345!");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserDto)))
                .andExpect(status().isBadRequest()) // 400 Bad Request
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldFailToAuthenticateWhenDataIsIncomplete() throws Exception {
        LoginUser incompleteLogin = new LoginUser("", "password123");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incompleteLogin)))
                .andExpect(status().isBadRequest()) // 400 Bad Request
                .andExpect(jsonPath("$.message").exists());
    }

}
