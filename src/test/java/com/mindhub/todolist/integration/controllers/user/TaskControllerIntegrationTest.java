package com.mindhub.todolist.integration.controllers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhub.todolist.config.JwtUtils;
import com.mindhub.todolist.dtos.TaskDto;
import com.mindhub.todolist.dtos.TaskInputDtoForUser;
import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.models.enums.TaskStatus;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.TaskService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private UserRepository userRepository;

    private TaskDto taskDto;
    private TaskInputDtoForUser taskInputDto;

    private final String validToken = "Bearer mocked-valid-jwt-token";

    @BeforeEach
    void setup() {
        Mockito.when(jwtUtils.validateToken(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(true);
        Mockito.when(jwtUtils.extractUsername(Mockito.anyString())).thenReturn("testuser@email.com");

        // Authenticated user mock
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("testuser@email.com");
        userEntity.setPassword("encoded-password");

        Mockito.when(userRepository.findByEmail("testuser@email.com"))
                .thenReturn(java.util.Optional.of(userEntity));

        // UserDetails mock with "USER" rol
        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(
                        "testuser@email.com",
                        "encoded-password",
                        List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("USER"))
                );

        // Entablish the security context with the authenticated user
        Authentication authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

        // User's task mock
        TaskEntity taskEntity = Mockito.mock(TaskEntity.class);
        Mockito.when(taskEntity.getId()).thenReturn(1L); // Simulates a generated ID
        Mockito.when(taskEntity.getTitle()).thenReturn("Test Task");
        Mockito.when(taskEntity.getDescription()).thenReturn("Description of test task");
        Mockito.when(taskEntity.getTaskStatus()).thenReturn(TaskStatus.PENDING);

        taskDto = new TaskDto(taskEntity);
        taskInputDto = new TaskInputDtoForUser("New Task", "New task description", TaskStatus.IN_PROGRESS);

        Mockito.when(taskService.getAllTasksForCurrentUser()).thenReturn(List.of(taskDto));
        Mockito.when(taskService.getUserEntityTaskById(1L)).thenReturn(taskDto);
        Mockito.when(taskService.createTaskForCurrentUserEntity(Mockito.any(TaskInputDtoForUser.class)))
                .thenReturn(taskDto);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldGetAllTasksForCurrentUser() throws Exception {
        mockMvc.perform(get("/api/user/tasks")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Task"))
                .andExpect(jsonPath("$[0].description").value("Description of test task"))
                .andExpect(jsonPath("$[0].taskStatus").value("PENDING"));
    }

    @Test
    void shouldFailToGetTasksWithoutToken() throws Exception {
        mockMvc.perform(get("/api/user/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Authorization header is missing"));
    }

    @Test
    void shouldCreateTaskForCurrentUser() throws Exception {
        mockMvc.perform(post("/api/user/tasks/create")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskInputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Description of test task"))
                .andExpect(jsonPath("$.taskStatus").value("PENDING"));
    }

    @Test
    void shouldFailToCreateTaskWithInvalidToken() throws Exception {
        String invalidToken = "Bearer invalid-token";

        Mockito.when(jwtUtils.validateToken(Mockito.eq("invalid-token"), Mockito.anyString()))
                .thenReturn(false);

        mockMvc.perform(post("/api/user/tasks/create")
                        .header("Authorization", invalidToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskInputDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid or expired token"));
    }

    @Test
    void shouldDeleteTaskForCurrentUser() throws Exception {
        mockMvc.perform(delete("/api/user/tasks/1")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldFailToDeleteTaskWithoutToken() throws Exception {
        mockMvc.perform(delete("/api/user/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Authorization header is missing"));
    }
}
