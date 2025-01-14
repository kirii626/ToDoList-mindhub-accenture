package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;

public class TaskInputDto {

    @NotBlank(message = "The title can´t be null")
    private String title;

    @NotBlank(message = "The description can´t be null")
    private String description;

    @NotBlank(message = "The status can´t be null")
    private TaskStatus tasksStatus;

    private Long usuarioId;

    public TaskInputDto() {
    }

    public TaskInputDto(String title, String description, TaskStatus tasksStatus, Long usuarioId) {
        this.title = title;
        this.description = description;
        this.tasksStatus = tasksStatus;
        this.usuarioId = usuarioId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getTasksStatus() {
        return tasksStatus;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }
}
