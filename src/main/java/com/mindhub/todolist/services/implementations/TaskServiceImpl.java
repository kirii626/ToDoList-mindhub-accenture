package com.mindhub.todolist.services.implementations;

import com.mindhub.todolist.dtos.TaskDto;
import com.mindhub.todolist.dtos.TaskInputDto;
import com.mindhub.todolist.exceptions.TaskNotFoundExc;
import com.mindhub.todolist.exceptions.UserNotFoundExc;
import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.models.Usuario;
import com.mindhub.todolist.models.enums.TaskStatus;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.repositories.UsuarioRepository;
import com.mindhub.todolist.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private final TaskRepository taskRepository;
    @Autowired
    private final UsuarioRepository usuarioRepository;

    public TaskServiceImpl(TaskRepository taskRepository, UsuarioRepository usuarioRepository) {
        this.taskRepository = taskRepository;
        this.usuarioRepository = usuarioRepository;
    }

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
    @Transactional
    public void deleteByTaskStatus(TaskStatus status) {
        if (!taskRepository.existsByTaskStatus(status)) {
            throw new RuntimeException("No tasks found with status: " + status);
        }
        taskRepository.deleteByTaskStatus(status);
    }
}
