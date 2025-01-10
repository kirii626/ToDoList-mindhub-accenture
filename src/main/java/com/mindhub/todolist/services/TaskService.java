package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.TaskDto;
import com.mindhub.todolist.dtos.TaskInputDto;
import com.mindhub.todolist.models.enums.TaskStatus;

import java.util.List;

public interface TaskService {

    List<TaskDto> getAllTasks();

    TaskDto getTaskById(Long id);

    TaskDto createTask(TaskInputDto taskInputDto);

    void deleteTask(Long id);

    TaskDto updateTask(Long id, TaskInputDto taskInputDto);

    List<TaskDto> getFindByTaskStatus(TaskStatus taskStatus);

    boolean getExistsByTitle(String title);

    Long getCountByUsuarioId(Long usuarioId);

    List<TaskDto> getfindByTitleOrderByIdAsc(String title);

    void deleteByTaskStatus(TaskStatus status);

    boolean getExistsByTaskStatus(TaskStatus taskStatus);
}
