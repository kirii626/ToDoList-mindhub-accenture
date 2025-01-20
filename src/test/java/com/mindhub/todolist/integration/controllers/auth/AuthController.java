package com.mindhub.todolist.integration.controllers.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhub.todolist.dtos.LoginUser;
import com.mindhub.todolist.dtos.NewUserDto;
import com.mindhub.todolist.dtos.UserDto;
import com.mindhub.todolist.exceptions.UserAlreadyExistsExc;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.models.enums.RoleName;
import com.mindhub.todolist.services.AuthService;
import com.mindhub.todolist.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserService userService;

    private NewUserDto newUserDto;
    private LoginUser loginUser;
    private UserEntity userEntity;

    @BeforeEach
    void setup() {
        // Register DTO
        newUserDto = new NewUserDto("testuser", "testuser@email.com", "password123");

        // Login DTO
        loginUser = new LoginUser("testuser@email.com", "password123");

        // Simulated user
        userEntity = new UserEntity();
        userEntity.setUsername("testuser");
        userEntity.setEmail("testuser@email.com");
        userEntity.setRoleName(RoleName.USER);

        // Service authentication mock
        Mockito.when(authService.registerNewUser(Mockito.any(NewUserDto.class)))
                .thenReturn(new UserDto(userEntity));

        Mockito.when(authService.authenticateAndGenerateToken(Mockito.anyString(), Mockito.anyString()))
                .thenReturn("mocked-jwt-token");
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("testuser@email.com"));
    }

    @Test
    void shouldFailToRegisterWhenUserAlreadyExists() throws Exception {
        Mockito.when(authService.registerNewUser(Mockito.any(NewUserDto.class)))
                .thenThrow(new UserAlreadyExistsExc("User already exists"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserDto)))
                .andExpect(status().isConflict()) // 409 Conflict
                .andExpect(jsonPath("$.message").value("User already exists"));
    }

    @Test
    void shouldAuthenticateUserSuccessfully() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUser)))
                .andExpect(status().isOk())
                .andExpect(content().string("mocked-jwt-token"));
    }

    @Test
    void shouldFailToAuthenticateWithInvalidCredentials() throws Exception {
        Mockito.when(authService.authenticateAndGenerateToken(Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new RuntimeException("Invalid email or password"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUser)))
                .andExpect(status().isUnauthorized()) // 401 Unauthorized
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    @Test
    void shouldFailToRegisterWithInvalidData() throws Exception {
        NewUserDto invalidUserDto = new NewUserDto("", "invalidemail", "");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserDto)))
                .andExpect(status().isBadRequest()); // 400 Bad Request
    }

    @Test
    void shouldFailToAuthenticateWithIncompleteData() throws Exception {
        LoginUser incompleteLogin = new LoginUser("", "");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incompleteLogin)))
                .andExpect(status().isBadRequest()); // 400 Bad Request
    }
}
