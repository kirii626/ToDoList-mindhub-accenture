package com.mindhub.todolist.integration.controllers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhub.todolist.config.JwtUtils;
import com.mindhub.todolist.dtos.TaskDto;
import com.mindhub.todolist.dtos.TaskInputDtoForUser;
import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.models.enums.TaskStatus;
import com.mindhub.todolist.repositories.TaskRepository;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
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

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    private UserEntity userEntity;
    private String validToken;

    @BeforeEach
    void setup() {
        userEntity = new UserEntity();
        userEntity.setUsername("testuser");
        userEntity.setEmail("testuser@email.com");
        userEntity.setPassword("encoded-password");
        userEntity = userRepository.save(userEntity);

        validToken = "Bearer " + jwtUtils.generateToken(userEntity.getEmail());

        User userDetails = new User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                List.of(new SimpleGrantedAuthority("USER"))
        );
        SecurityContextHolder.setContext(new SecurityContextImpl(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                )
        ));

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle("Initial Task");
        taskEntity.setDescription("Initial task description");
        taskEntity.setTaskStatus(TaskStatus.PENDING);
        taskEntity.setUserEntity(userEntity);
        taskRepository.save(taskEntity);
    }

    @AfterEach
    void teardown() {
        SecurityContextHolder.clearContext();
        taskRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldGetAllTasksForCurrentUser() throws Exception {
        mockMvc.perform(get("/api/user/tasks")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Initial Task"))
                .andExpect(jsonPath("$[0].description").value("Initial task description"))
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
        TaskInputDtoForUser taskInputDto = new TaskInputDtoForUser("New Task", "Description of new task", TaskStatus.IN_PROGRESS);

        mockMvc.perform(post("/api/user/tasks/create")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskInputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.description").value("Description of new task"))
                .andExpect(jsonPath("$.taskStatus").value("IN_PROGRESS"));
    }

    @Test
    void shouldFailToCreateTaskWithInvalidToken() throws Exception {
        TaskInputDtoForUser taskInputDto = new TaskInputDtoForUser("New Task", "Description of new task", TaskStatus.IN_PROGRESS);

        mockMvc.perform(post("/api/user/tasks/create")
                        .header("Authorization", "Bearer invalid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskInputDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid or expired token"));
    }

    @Test
    void shouldDeleteTaskForCurrentUser() throws Exception {
        TaskEntity taskEntity = taskRepository.findAll().get(0);

        mockMvc.perform(delete("/api/user/tasks/" + taskEntity.getId())
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldFailToDeleteTaskWithoutToken() throws Exception {
        TaskEntity taskEntity = taskRepository.findAll().get(0);

        mockMvc.perform(delete("/api/user/tasks/" + taskEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Authorization header is missing"));
    }
}
