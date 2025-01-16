package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.enums.TaskStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TaskInputDtoForUser {

    @NotBlank(message = "The title can´t be null")
    private String title;

    @NotBlank(message = "The description can´t be null")
    private String description;

    @NotNull(message = "The status can´t be null")
    private TaskStatus tasksStatus;

    public TaskInputDtoForUser() {
    }

    public TaskInputDtoForUser(String title, String description, TaskStatus tasksStatus) {
        this.title = title;
        this.description = description;
        this.tasksStatus = tasksStatus;
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
}
