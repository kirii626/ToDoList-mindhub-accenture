package com.mindhub.todolist.integration.services;

import com.mindhub.todolist.dtos.TaskDto;
import com.mindhub.todolist.dtos.TaskInputDto;
import com.mindhub.todolist.exceptions.TaskNotFoundExc;
import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.models.enums.RoleName;
import com.mindhub.todolist.models.enums.TaskStatus;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
public class TaskServiceIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new UserEntity("testuser", "test@example.com", RoleName.USER, "password123");
        userRepository.save(testUser);
    }

    // Test: Create task for an existing user - successful
    @Test
    void testCreateTaskSuccess() {
        TaskInputDto taskInputDto = new TaskInputDto("Nueva tarea", "Descripción de tarea", TaskStatus.PENDING, testUser.getId());

        TaskDto createdTask = taskService.createTask(taskInputDto);

        assertThat(createdTask.getTitle()).isEqualTo("Nueva tarea");
        assertThat(createdTask.getDescription()).isEqualTo("Descripción de tarea");
    }

    // Test: Create task for an existing user - Unsuccessful
    @Test
    void testCreateTaskUserNotFound() {
        TaskInputDto taskInputDto = new TaskInputDto("Nueva tarea", "Descripción de tarea", TaskStatus.PENDING, 999L);

        assertThatThrownBy(() -> taskService.createTask(taskInputDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found by ID");
    }

    // Test: Retrieve task by ID
    @Test
    void testGetTaskByIdSuccess() {
        TaskEntity task = new TaskEntity("Tarea 1", "Descripción 1", TaskStatus.PENDING);
        task.setUserEntity(testUser);
        taskRepository.save(task);

        TaskDto result = taskService.getTaskById(task.getId());

        assertThat(result.getTitle()).isEqualTo("Tarea 1");
    }

    // Test: Retrieve inexistent task by ID
    @Test
    void testGetTaskByIdNotFound() {
        assertThatThrownBy(() -> taskService.getTaskById(999L))
                .isInstanceOf(TaskNotFoundExc.class)
                .hasMessageContaining("Task not found with ID: 999");
    }

    // Test: Delete existing task - successful
    @Test
    void testDeleteTaskSuccess() {
        TaskEntity task = new TaskEntity("Tarea a eliminar", "Descripción", TaskStatus.PENDING);
        task.setUserEntity(testUser);
        taskRepository.save(task);

        taskService.deleteTask(task.getId());

        assertThat(taskRepository.findById(task.getId())).isNotPresent();
    }

    // Test: Delete existing task - unsuccessful
    @Test
    void testDeleteTaskNotFound() {
        assertThatThrownBy(() -> taskService.deleteTask(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Task not found by ID");
    }

    // Test: Update existing task - successful
    @Test
    void testUpdateTaskSuccess() {
        TaskEntity task = new TaskEntity("Tarea vieja", "Descripción vieja", TaskStatus.PENDING);
        task.setUserEntity(testUser);
        taskRepository.save(task);

        TaskInputDto updatedDto = new TaskInputDto("Tarea actualizada", "Descripción actualizada", TaskStatus.COMPLETED, testUser.getId());

        TaskDto updatedTask = taskService.updateTask(task.getId(), updatedDto);

        assertThat(updatedTask.getTitle()).isEqualTo("Tarea actualizada");
        assertThat(updatedTask.getTaskStatus()).isEqualTo(TaskStatus.COMPLETED);
    }

    // Test: Filter tasks based on status
    @Test
    void testGetFindByTaskStatus() {
        taskRepository.save(new TaskEntity("Tarea 1", "Descripción 1", TaskStatus.PENDING));
        taskRepository.save(new TaskEntity("Tarea 2", "Descripción 2", TaskStatus.COMPLETED));

        List<TaskDto> pendingTasks = taskService.getFindByTaskStatus(TaskStatus.PENDING);

        assertThat(pendingTasks).hasSize(1);
        assertThat(pendingTasks.get(0).getTaskStatus()).isEqualTo(TaskStatus.PENDING);
    }

    // Test: Validate title existence
    @Test
    void testGetExistsByTitle() {
        taskRepository.save(new TaskEntity("Título único", "Descripción", TaskStatus.PENDING));

        boolean exists = taskService.getExistsByTitle("Título único");

        assertThat(exists).isTrue();
    }

    // Test: Retrieve tasks for the authenticated user
    @Test
    @WithMockUser(username = "test@example.com")
    void testGetAllTasksForCurrentUser() {
        TaskEntity task = new TaskEntity();
        task.setTitle("Tarea de usuario");
        task.setDescription("Descripción");
        task.setTaskStatus(TaskStatus.PENDING);
        task.setUserEntity(testUser);

        taskRepository.save(task);

        List<TaskDto> tasks = taskService.getAllTasksForCurrentUser();

        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Tarea de usuario");
    }
}
