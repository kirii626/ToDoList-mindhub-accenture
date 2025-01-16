package com.mindhub.todolist.services.implementations;

import com.mindhub.todolist.dtos.TaskDto;
import com.mindhub.todolist.dtos.TaskInputDto;
import com.mindhub.todolist.dtos.TaskInputDtoForUser;
import com.mindhub.todolist.exceptions.TaskNotFoundExc;
import com.mindhub.todolist.exceptions.UserNotFoundExc;
import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.models.Usuario;
import com.mindhub.todolist.models.enums.TaskStatus;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.repositories.UsuarioRepository;
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
    private UsuarioRepository usuarioRepository;

    @Override
    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(TaskDto::new)
                .toList();
    }

    @Override
    public TaskDto getTaskById(Long id) {
        Task task =  taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundExc("Task not found with ID: " + id));
        return new TaskDto(task);
    }

    @Override
    public TaskDto createTask(TaskInputDto taskInputDto) {
        Task task = new Task();
        task.setTitle(taskInputDto.getTitle());
        task.setDescription(taskInputDto.getDescription());
        task.setTaskStatus(taskInputDto.getTasksStatus());

        if (taskInputDto.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(taskInputDto.getUsuarioId())
                    .orElseThrow(() -> new UserNotFoundExc("User not found by ID: "));
            task.setUsuario(usuario);
        }

        Task savedTask = taskRepository.save(task);
        return new TaskDto(savedTask);
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found by ID: ");
        }
        taskRepository.deleteById(id);
    }

    @Override
    public TaskDto updateTask(Long id, TaskInputDto taskInputDto) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        existingTask.setTitle(taskInputDto.getTitle());
        existingTask.setDescription(taskInputDto.getDescription());
        existingTask.setTaskStatus(taskInputDto.getTasksStatus());

        if (taskInputDto.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(taskInputDto.getUsuarioId())
                    .orElseThrow(() -> new UserNotFoundExc("User not found by ID: "));
            existingTask.setUsuario(usuario);
        }

        Task updatedTask = taskRepository.save(existingTask);
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
    public Long getCountByUsuarioId(Long usuarioId) {
        return taskRepository.countByUsuarioId(usuarioId);
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
        Usuario usuario = getAuthenticatedUsuarioEntity();
        return taskRepository.findByUsuario(usuario)
                .stream()
                .map(TaskDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto getUserTaskById(Long id) {
        Usuario usuario = getAuthenticatedUsuarioEntity();
        Task task = taskRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new TaskNotFoundExc("Task not found with ID: " + id));
        return new TaskDto(task);
    }

    @Override
    public TaskDto updateUserTask(Long id, TaskInputDtoForUser taskInputDtoForUser) {
        Usuario usuario = getAuthenticatedUsuarioEntity();
        Task task = taskRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new TaskNotFoundExc("Task not found with ID: " + id));

        task.setTitle(taskInputDtoForUser.getTitle());
        task.setDescription(taskInputDtoForUser.getDescription());
        task.setTaskStatus(taskInputDtoForUser.getTasksStatus());

        Task updatedTask = taskRepository.save(task);
        return new TaskDto(updatedTask);
    }

    @Override
    @Transactional
    public void deleteUserTask(Long id) {
        Usuario usuario = getAuthenticatedUsuarioEntity();
        Task task = taskRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new TaskNotFoundExc("Task not found with ID: " + id));

        taskRepository.delete(task);
    }

    @Override
    public TaskDto createTaskForCurrentUser(TaskInputDtoForUser taskInputDtoForUser) {
        Usuario usuario = getAuthenticatedUsuarioEntity();

        Task task = new Task();
        task.setTitle(taskInputDtoForUser.getTitle());
        task.setDescription(taskInputDtoForUser.getDescription());
        task.setTaskStatus(taskInputDtoForUser.getTasksStatus());
        task.setUsuario(usuario);

        Task savedTask = taskRepository.save(task);
        return new TaskDto(savedTask);
    }

    @Override
    @Transactional
    public void deleteByTaskStatus(TaskStatus status) {
        if (!taskRepository.existsByTaskStatus(status)) {
            throw new RuntimeException("No tasks found with status: " + status);
        }
        taskRepository.deleteByTaskStatus(status);
    }

    private Usuario getAuthenticatedUsuarioEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundExc("User not found by email: " + email));
    }
}
