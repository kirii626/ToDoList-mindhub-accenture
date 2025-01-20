package com.mindhub.todolist.integration.repositories;

import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.models.enums.TaskStatus;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class TaskRepositoryIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity user;
    
    private TaskEntity task1;
    
    private TaskEntity task2;

    @BeforeEach
    void setUp() {
        // Create and save a user
        user = new UserEntity();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password123");
        userRepository.save(user);

        // Create and save tasks
        task1 = new TaskEntity();
        task1.setTitle("Test Task 1");
        task1.setDescription("Description for Task 1");
        task1.setTaskStatus(TaskStatus.PENDING);
        task1.setUserEntity(user);
        taskRepository.save(task1);

        task2 = new TaskEntity();
        task2.setTitle("Test Task 2");
        task2.setDescription("Description for Task 2");
        task2.setTaskStatus(TaskStatus.COMPLETED);
        task2.setUserEntity(user);
        taskRepository.save(task2);
    }

    @Test
    void findByTaskStatusShouldReturnTasksByStatus() {
        List<TaskEntity> pendingTasks = taskRepository.findByTaskStatus(TaskStatus.PENDING);
        assertEquals(1, pendingTasks.size());
        assertEquals("Test Task 1", pendingTasks.get(0).getTitle());

        List<TaskEntity> completedTasks = taskRepository.findByTaskStatus(TaskStatus.COMPLETED);
        assertEquals(1, completedTasks.size());
        assertEquals("Test Task 2", completedTasks.get(0).getTitle());
    }

    @Test
    void existsByTitleShouldReturnTrueIfTitleExists() {
        assertTrue(taskRepository.existsByTitle("Test Task 1"));
        assertFalse(taskRepository.existsByTitle("Nonexistent Task"));
    }

    @Test
    void countByUserEntityIdShouldReturnTaskCountForUser() {
        Long taskCount = taskRepository.countByUserEntityId(user.getId());
        assertEquals(2, taskCount);
    }

    @Test
    void findByTitleOrderByIdAscShouldReturnTasksOrderedById() {
        List<TaskEntity> tasks = taskRepository.findByTitleOrderByIdAsc("Test Task 1");
        assertEquals(1, tasks.size());
        assertEquals("Test Task 1", tasks.get(0).getTitle());
    }

    @Test
    void deleteByTaskStatusShouldDeleteTasksByStatus() {
        taskRepository.deleteByTaskStatus(TaskStatus.PENDING);
        List<TaskEntity> tasks = taskRepository.findByTaskStatus(TaskStatus.PENDING);
        assertTrue(tasks.isEmpty());
    }

    @Test
    void existsByTaskStatusShouldReturnTrueIfTaskStatusExists() {
        assertTrue(taskRepository.existsByTaskStatus(TaskStatus.PENDING));
        assertFalse(taskRepository.existsByTaskStatus(TaskStatus.IN_PROGRESS));
    }

    @Test
    void findByUserEntityShouldReturnTasksForUser() {
        List<TaskEntity> tasks = taskRepository.findByUserEntity(user);
        assertEquals(2, tasks.size());
        assertEquals("Test Task 1", tasks.get(0).getTitle());
        assertEquals("Test Task 2", tasks.get(1).getTitle());
    }

    @Test
    void findByIdAndUserEntityShouldReturnTaskByIdAndUser() {
        Optional<TaskEntity> task = taskRepository.findByIdAndUserEntity(task1.getId(), user);
        assertTrue(task.isPresent());
        assertEquals("Test Task 1", task.get().getTitle());
    }

    @Test
    void findByIdAndUserEntityShouldReturnEmptyIfTaskNotBelongToUser() {
        UserEntity anotherUser = new UserEntity();
        anotherUser.setUsername("anotheruser");
        anotherUser.setEmail("anotheruser@example.com");
        anotherUser.setPassword("password456");
        userRepository.save(anotherUser);

        Optional<TaskEntity> task = taskRepository.findByIdAndUserEntity(task1.getId(), anotherUser);
        assertFalse(task.isPresent());
    }
}
