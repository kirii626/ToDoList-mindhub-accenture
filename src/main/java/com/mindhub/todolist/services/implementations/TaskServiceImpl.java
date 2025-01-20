package com.mindhub.todolist.services.implementations;

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
import com.mindhub.todolist.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(TaskDto::new)
                .toList();
    }

    @Override
    public TaskDto getTaskById(Long id) {
        TaskEntity taskEntity =  taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundExc("Task not found with ID: " + id));
        return new TaskDto(taskEntity);
    }

    @Override
    public TaskDto createTask(TaskInputDto taskInputDto) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(taskInputDto.getTitle());
        taskEntity.setDescription(taskInputDto.getDescription());
        taskEntity.setTaskStatus(taskInputDto.getTasksStatus());

        if (taskInputDto.getUserId() != null) {
            UserEntity userEntity = userRepository.findById(taskInputDto.getUserId())
                    .orElseThrow(() -> new UserNotFoundExc("User not found by ID: "));
            taskEntity.setUserEntity(userEntity);
        }

        TaskEntity savedTask = taskRepository.save(taskEntity);
        return new TaskDto(savedTask);
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundExc("Task not found by ID: ");
        }
        taskRepository.deleteById(id);
    }

    @Override
    public TaskDto updateTask(Long id, TaskInputDto taskInputDto) {
        TaskEntity existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        existingTask.setTitle(taskInputDto.getTitle());
        existingTask.setDescription(taskInputDto.getDescription());
        existingTask.setTaskStatus(taskInputDto.getTasksStatus());

        if (taskInputDto.getUserId() != null) {
            UserEntity userEntity = userRepository.findById(taskInputDto.getUserId())
                    .orElseThrow(() -> new UserNotFoundExc("User not found by ID: "));
            existingTask.setUserEntity(userEntity);
        }

        TaskEntity updatedTask = taskRepository.save(existingTask);
        return new TaskDto(updatedTask);
    }

    @Override
    public List<TaskDto> getFindByTaskStatus(TaskStatus taskStatus) {
        return taskRepository.findByTaskStatus(taskStatus)
                .stream()
                .map(TaskDto::new)
                .toList();
    }

    @Override
    public boolean getExistsByTitle(String title) {
        return taskRepository.existsByTitle(title);
    }

    @Override
    public Long getCountByUserEntityId(Long userId) {
        return taskRepository.countByUserEntityId(userId);
    }

    @Override
    public List<TaskDto> getfindByTitleOrderByIdAsc(String title) {
        return taskRepository.findByTitleOrderByIdAsc(title)
                .stream()
                .map(TaskDto::new)
                .toList();
    }

    @Override
    public boolean getExistsByTaskStatus(TaskStatus taskStatus) {
        return taskRepository.existsByTaskStatus(taskStatus);
    }

    @Override
    public List<TaskDto> getAllTasksForCurrentUser() {
        UserEntity userEntity = getAuthenticatedUserEntity();
        return taskRepository.findByUserEntity(userEntity)
                .stream()
                .map(TaskDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto getUserEntityTaskById(Long id) {
        UserEntity userEntity = getAuthenticatedUserEntity();
        TaskEntity taskEntity = taskRepository.findByIdAndUserEntity(id, userEntity)
                .orElseThrow(() -> new TaskNotFoundExc("Task not found with ID: " + id));
        return new TaskDto(taskEntity);
    }

    @Override
    public TaskDto updateUserEntityTask(Long id, TaskInputDtoForUser taskInputDtoForUser) {
        UserEntity userEntity = getAuthenticatedUserEntity();
        TaskEntity taskEntity = taskRepository.findByIdAndUserEntity(id, userEntity)
                .orElseThrow(() -> new TaskNotFoundExc("Task not found with ID: " + id));

        taskEntity.setTitle(taskInputDtoForUser.getTitle());
        taskEntity.setDescription(taskInputDtoForUser.getDescription());
        taskEntity.setTaskStatus(taskInputDtoForUser.getTasksStatus());

        TaskEntity updatedTask = taskRepository.save(taskEntity);
        return new TaskDto(updatedTask);
    }

    @Override
    @Transactional
    public void deleteUserEntityTask(Long id) {
        UserEntity userEntity = getAuthenticatedUserEntity();
        TaskEntity taskEntity = taskRepository.findByIdAndUserEntity(id, userEntity)
                .orElseThrow(() -> new TaskNotFoundExc("Task not found with ID: " + id));

        taskRepository.delete(taskEntity);
    }

    @Override
    public TaskDto createTaskForCurrentUserEntity(TaskInputDtoForUser taskInputDtoForUser) {
        UserEntity userEntity = getAuthenticatedUserEntity();

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(taskInputDtoForUser.getTitle());
        taskEntity.setDescription(taskInputDtoForUser.getDescription());
        taskEntity.setTaskStatus(taskInputDtoForUser.getTasksStatus());
        taskEntity.setUserEntity(userEntity);

        TaskEntity savedTask = taskRepository.save(taskEntity);
        return new TaskDto(savedTask);
    }

    @Override
    @Transactional
    public void deleteByTaskStatus(TaskStatus status) {
        if (!taskRepository.existsByTaskStatus(status)) {
            throw new TaskNotFoundExc("No tasks found with status: " + status);
        }
        taskRepository.deleteByTaskStatus(status);
    }

    private UserEntity getAuthenticatedUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundExc("User not found by email: " + email));
    }
}
