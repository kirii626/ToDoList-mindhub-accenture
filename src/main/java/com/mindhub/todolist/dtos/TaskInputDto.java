package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TaskInputDto {

    @NotBlank(message = "titulo")
    private String title;

    @NotBlank(message = "The description can´t be null")
    private String description;

    @NotNull(message = "The task status can't be blank. Allowed values: PENDING, IN_PROGRESS, COMPLETED")
    private TaskStatus tasksStatus;

    private Long userId;

    public TaskInputDto() {
    }

    public TaskInputDto(String title, String description, TaskStatus tasksStatus, Long userId) {
        this.title = title;
        this.description = description;
        this.tasksStatus = tasksStatus;
        this.userId = userId;
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

    public Long getUserId() {
        return userId;
    }
}
