package com.mindhub.todolist.dtos;


import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.models.Usuario;
import com.mindhub.todolist.models.enums.TaskStatus;


public class TaskDto {

    private Long id;

    private String title, description;

    private TaskStatus tasksStatus;

    public TaskDto(Task task) {
        id = task.getId();
        title = task.getTitle();
        description = task.getDescription();
        tasksStatus = task.getTaskStatus();
    }

    public Long getId() {
        return id;
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
