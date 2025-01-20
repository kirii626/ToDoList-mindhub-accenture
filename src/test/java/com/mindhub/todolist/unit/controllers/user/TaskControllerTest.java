package com.mindhub.todolist.unit.controllers.user;


import com.mindhub.todolist.controllers.user.TaskController;
import com.mindhub.todolist.dtos.TaskDto;
import com.mindhub.todolist.dtos.TaskInputDtoForUser;
import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    private TaskEntity taskEntity;
    private TaskInputDtoForUser taskInputDtoForUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock data
        taskEntity = new TaskEntity();
        taskEntity.setTitle("Test Task");
        taskEntity.setDescription("Test Task Description");

        taskInputDtoForUser = new TaskInputDtoForUser("New Task", "New Task Description", null);
    }

    @Test
    void getAllTasksSuccess() {
        // Mock service call
        when(taskService.getAllTasksForCurrentUser()).thenReturn(List.of(new TaskDto(taskEntity)));

        // Call controller
        ResponseEntity<List<TaskDto>> response = taskController.getAllTasks();

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Task", response.getBody().get(0).getTitle());

        // Verify interaction with service
        verify(taskService, times(1)).getAllTasksForCurrentUser();
    }

    @Test
    void getTaskByIdSuccess() {
        // Mock service call
        when(taskService.getUserEntityTaskById(1L)).thenReturn(new TaskDto(taskEntity));

        // Call controller
        ResponseEntity<TaskDto> response = taskController.getTaskById(1L);

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Test Task", response.getBody().getTitle());

        // Verify interaction with service
        verify(taskService, times(1)).getUserEntityTaskById(1L);
    }

    @Test
    void getTaskByIdNotFound() {
        // Mock service call
        when(taskService.getUserEntityTaskById(1L)).thenThrow(new RuntimeException("Task not found"));

        // Call controller
        Exception exception = assertThrows(RuntimeException.class, () -> taskController.getTaskById(1L));

        // Assertions
        assertEquals("Task not found", exception.getMessage());

        // Verify interaction with service
        verify(taskService, times(1)).getUserEntityTaskById(1L);
    }

    @Test
    void createTaskForCurrentUserSuccess() {
        // Mock service call
        when(taskService.createTaskForCurrentUserEntity(taskInputDtoForUser)).thenReturn(new TaskDto(taskEntity));

        // Call controller
        ResponseEntity<TaskDto> response = taskController.createTaskForCurrentUser(taskInputDtoForUser);

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Test Task", response.getBody().getTitle());

        // Verify interaction with service
        verify(taskService, times(1)).createTaskForCurrentUserEntity(taskInputDtoForUser);
    }

    @Test
    void updateTaskSuccess() {
        // Mock service call
        when(taskService.updateUserEntityTask(1L, taskInputDtoForUser)).thenReturn(new TaskDto(taskEntity));

        // Call controller
        ResponseEntity<TaskDto> response = taskController.updateTask(1L, taskInputDtoForUser);

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Test Task", response.getBody().getTitle());

        // Verify interaction with service
        verify(taskService, times(1)).updateUserEntityTask(1L, taskInputDtoForUser);
    }

    @Test
    void deleteTaskSuccess() {
        // Call controller
        ResponseEntity<Void> response = taskController.deleteTask(1L);

        // Assertions
        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());

        // Verify interaction with service
        verify(taskService, times(1)).deleteUserEntityTask(1L);
    }

    @Test
    void deleteTaskNotFound() {
        // Mock service call
        doThrow(new RuntimeException("Task not found")).when(taskService).deleteUserEntityTask(1L);

        // Call controller
        Exception exception = assertThrows(RuntimeException.class, () -> taskController.deleteTask(1L));

        // Assertions
        assertEquals("Task not found", exception.getMessage());

        // Verify interaction with service
        verify(taskService, times(1)).deleteUserEntityTask(1L);
    }
}
