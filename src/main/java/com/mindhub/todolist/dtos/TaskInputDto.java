package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.enums.TaskStatus;

public class TaskInputDto {

    private String title;
    private String description;
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
