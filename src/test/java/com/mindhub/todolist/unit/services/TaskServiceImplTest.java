package com.mindhub.todolist.unit.services;

import com.mindhub.todolist.dtos.TaskDto;
import com.mindhub.todolist.dtos.TaskInputDto;
import com.mindhub.todolist.dtos.TaskInputDtoForUser;
import com.mindhub.todolist.exceptions.TaskNotFoundExc;
import com.mindhub.todolist.exceptions.UserNotFoundExc;
import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.models.enums.TaskStatus;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.implementations.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class TaskServiceImplTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private TaskEntity taskEntity;
    private UserEntity userEntity;
    private TaskInputDto taskInputDto;
    private TaskInputDtoForUser taskInputDtoForUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock Security Context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        userEntity = new UserEntity();
        userEntity.setEmail("testuser@email.com");
        userEntity.setUsername("testuser");

        taskEntity = new TaskEntity();
        taskEntity.setTitle("Test Task");
        taskEntity.setDescription("Description of test task");
        taskEntity.setTaskStatus(TaskStatus.PENDING);
        taskEntity.setUserEntity(userEntity);

        taskInputDto = new TaskInputDto("New Task", "New task description", TaskStatus.IN_PROGRESS, 1L);
        taskInputDtoForUser = new TaskInputDtoForUser("New Task", "New task description", TaskStatus.IN_PROGRESS);

        when(authentication.getName()).thenReturn("testuser@email.com");
    }

    // Test retrieving all tasks successfully
    @Test
    void getAllTasksSuccess() {
        when(taskRepository.findAll()).thenReturn(List.of(taskEntity));

        List<TaskDto> tasks = taskService.getAllTasks();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    // Test retrieving a task by its ID successfully
    @Test
    void getTaskByIdSuccess() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));

        TaskDto task = taskService.getTaskById(1L);

        assertNotNull(task);
        assertEquals("Test Task", task.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    // Test retrieving a task by its ID when the task does not exist
    @Test
    void getTaskByIdTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundExc.class, () -> taskService.getTaskById(1L));
        verify(taskRepository, times(1)).findById(1L);
    }

    // Test creating a task successfully
    @Test
    void createTaskSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(taskEntity);

        TaskDto createdTask = taskService.createTask(taskInputDto);

        assertNotNull(createdTask);
        assertEquals("Test Task", createdTask.getTitle());
        verify(taskRepository, times(1)).save(any(TaskEntity.class));
    }

    // Test creating a task when the user does not exist
    @Test
    void createTaskUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundExc.class, () -> taskService.createTask(taskInputDto));
        verify(userRepository, times(1)).findById(1L);
    }

    // Test deleting a task successfully
    @Test
    void deleteTaskSuccess() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    // Test deleting a task when the task does not exist
    @Test
    void deleteTaskTaskNotFound() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        assertThrows(TaskNotFoundExc.class, () -> taskService.deleteTask(1L));
        verify(taskRepository, times(1)).existsById(1L);
    }

    // Test retrieving all tasks for the authenticated user successfully
    @Test
    void getAllTasksForCurrentUserSuccess() {
        when(userRepository.findByEmail("testuser@email.com")).thenReturn(Optional.of(userEntity));
        when(taskRepository.findByUserEntity(userEntity)).thenReturn(List.of(taskEntity));

        List<TaskDto> tasks = taskService.getAllTasksForCurrentUser();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());
        verify(taskRepository, times(1)).findByUserEntity(userEntity);
    }

    // Test retrieving all tasks for the authenticated user when the user does not exist
    @Test
    void getAllTasksForCurrentUserUserNotFound() {
        when(userRepository.findByEmail("testuser@email.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundExc.class, () -> taskService.getAllTasksForCurrentUser());
        verify(userRepository, times(1)).findByEmail("testuser@email.com");
    }

    // Test updating a task for the authenticated user successfully
    @Test
    void updateUserEntityTaskSuccess() {
        when(userRepository.findByEmail("testuser@email.com")).thenReturn(Optional.of(userEntity));
        when(taskRepository.findByIdAndUserEntity(1L, userEntity)).thenReturn(Optional.of(taskEntity));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(taskEntity);

        TaskDto updatedTask = taskService.updateUserEntityTask(1L, taskInputDtoForUser);

        assertNotNull(updatedTask);
        assertEquals("New Task", updatedTask.getTitle());
        verify(taskRepository, times(1)).findByIdAndUserEntity(1L, userEntity);
        verify(taskRepository, times(1)).save(any(TaskEntity.class));
    }

    // Test updating a task for the authenticated user when the task does not exist
    @Test
    void updateUserEntityTaskTaskNotFound() {
        when(userRepository.findByEmail("testuser@email.com")).thenReturn(Optional.of(userEntity));
        when(taskRepository.findByIdAndUserEntity(1L, userEntity)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundExc.class, () -> taskService.updateUserEntityTask(1L, taskInputDtoForUser));
        verify(taskRepository, times(1)).findByIdAndUserEntity(1L, userEntity);
    }
}
