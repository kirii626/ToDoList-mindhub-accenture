package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.TaskDto;
import com.mindhub.todolist.dtos.TaskInputDto;
import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.models.enums.TaskStatus;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.repositories.UsuarioRepository;
import com.mindhub.todolist.services.TaskService;
import com.mindhub.todolist.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private final TaskService taskService;

    @Autowired
    private final UsuarioService usuarioService;

    public TaskController(TaskService taskService, UsuarioService usuarioService) {
        this.taskService = taskService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<TaskDto> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public TaskDto getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping
    public TaskDto createTask(@RequestBody TaskInputDto taskInputDto) {
        return taskService.createTask(taskInputDto);
    }

    @PutMapping("/{id}")
    public TaskDto updateTask(@PathVariable Long id, @RequestBody TaskInputDto taskInputDto) {
        return taskService.updateTask(id, taskInputDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @GetMapping("/by-status/{taskStatus}")
    public List<TaskDto> getFindByTaskStatus(@PathVariable TaskStatus taskStatus) {
        return taskService.getFindByTaskStatus(taskStatus);
    }

    @GetMapping("/by-title/{title}")
    public boolean getExistsByTitle(@PathVariable String title) {
        return taskService.getExistsByTitle(title);
    }

    @GetMapping("/count/{usuarioId}")
    public Long getCountByUsuarioId(@PathVariable Long usuarioId) {
        return taskService.getCountByUsuarioId(usuarioId);
    }

    @GetMapping("/filter/{title}")
    public List<TaskDto> getfindByTitleOrderByIdAsc(@PathVariable String title) {
        return taskService.getfindByTitleOrderByIdAsc(title);
    }

    @DeleteMapping("/delete/{status}")
    public void deleteByTaskStatus(@PathVariable TaskStatus status) {
        taskService.deleteByTaskStatus(status);
    }

}
