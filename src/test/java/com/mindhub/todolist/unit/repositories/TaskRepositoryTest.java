package com.mindhub.todolist.unit.repositories;

import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.models.enums.RoleName;
import com.mindhub.todolist.models.enums.TaskStatus;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        when(passwordEncoder.encode(org.mockito.ArgumentMatchers.anyString()))
                .thenReturn("mockedPassword");

        // Crear usuario de prueba
        testUser = new UserEntity("testuser", "test@example.com", RoleName.USER, passwordEncoder.encode("12345"));
        userRepository.save(testUser);

        // Crear tareas de prueba
        TaskEntity task1 = new TaskEntity("Tarea 1", "Descripción 1", TaskStatus.PENDING);
        TaskEntity task2 = new TaskEntity("Tarea 2", "Descripción 2", TaskStatus.COMPLETED);
        TaskEntity task3 = new TaskEntity("Tarea 1", "Otra descripción", TaskStatus.PENDING);

        task1.setUserEntity(testUser);
        task2.setUserEntity(testUser);
        task3.setUserEntity(testUser);

        taskRepository.saveAll(List.of(task1, task2, task3));
    }

    // Test for existsByTitle - Task found
    @Test
    void testExistsByTitleExists() {
        boolean exists = taskRepository.existsByTitle("Tarea 1");
        assertThat(exists).isTrue();
    }

    // Test for existsByTitle - Task inexistent
    @Test
    void testExistsByTitleNotExists() {
        boolean exists = taskRepository.existsByTitle("Tarea inexistente");
        assertThat(exists).isFalse();
    }

    // Test for countByUserEntityId
    @Test
    void testCountByUserEntityId() {
        Long count = taskRepository.countByUserEntityId(testUser.getId());
        assertThat(count).isEqualTo(3);
    }

    // Test for findByTitleOrderByIdAsc
    @Test
    void testFindByTitleOrderByIdAsc() {
        List<TaskEntity> tasks = taskRepository.findByTitleOrderByIdAsc("Tarea 1");
        assertThat(tasks).hasSize(2);
        assertThat(tasks.get(0).getId()).isLessThan(tasks.get(1).getId());
    }

    // Test for existsByTaskStatus
    @Test
    void testExistsByTaskStatus_Exists() {
        boolean exists = taskRepository.existsByTaskStatus(TaskStatus.COMPLETED);
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByTaskStatus_NotExists() {
        boolean exists = taskRepository.existsByTaskStatus(TaskStatus.IN_PROGRESS);
        assertThat(exists).isFalse();
    }

    // Test for findByUserEntity
    @Test
    void testFindByUserEntity() {
        List<TaskEntity> userTasks = taskRepository.findByUserEntity(testUser);
        assertThat(userTasks).hasSize(3);
    }

    // Test for findByIdAndUserEntity - Task Found
    @Test
    void testFindByIdAndUserEntity_Exists() {
        TaskEntity task = taskRepository.findByTitleOrderByIdAsc("Tarea 1").get(0);
        Optional<TaskEntity> result = taskRepository.findByIdAndUserEntity(task.getId(), testUser);

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Tarea 1");
    }

    // Test for findByIdAndUserEntity - Task not found
    @Test
    void testFindByIdAndUserEntity_NotExists() {
        Optional<TaskEntity> result = taskRepository.findByIdAndUserEntity(999L, testUser);
        assertThat(result).isNotPresent();
    }

    // Test for deleteByTaskStatus
    @Test
    void testDeleteByTaskStatus() {
        taskRepository.deleteByTaskStatus(TaskStatus.PENDING);

        List<TaskEntity> pendingTasks = taskRepository.findByTaskStatus(TaskStatus.PENDING);
        assertThat(pendingTasks).isEmpty();

        List<TaskEntity> completedTasks = taskRepository.findByTaskStatus(TaskStatus.COMPLETED);
        assertThat(completedTasks).hasSize(1);
    }
}
