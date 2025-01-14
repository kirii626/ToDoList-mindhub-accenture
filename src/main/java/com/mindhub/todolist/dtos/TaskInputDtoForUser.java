package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;

public class TaskInputDtoForUser {

    @NotBlank(message = "The title can´t be null")
    private String title;

    @NotBlank(message = "The description can´t be null")
    private String description;

    @NotBlank(message = "The status can´t be null")
    private TaskStatus tasksStatus;

    public TaskInputDtoForUser() {
    }

    public TaskInputDtoForUser(String title, String description, TaskStatus tasksStatus) {
        this.title = title;
        this.description = description;
        this.tasksStatus = tasksStatus;
    }

    public @NotBlank(message = "The title can´t be null") String getTitle() {
        return title;
    }

    public @NotBlank(message = "The description can´t be null") String getDescription() {
        return description;
    }

    public @NotBlank(message = "The status can´t be null") TaskStatus getTasksStatus() {
        return tasksStatus;
    }
}
