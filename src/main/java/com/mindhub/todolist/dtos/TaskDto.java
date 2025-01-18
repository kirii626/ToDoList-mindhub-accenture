package com.mindhub.todolist.dtos;


import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.enums.TaskStatus;


public class TaskDto {

    private Long id;

    private String title, description;

    private TaskStatus tasksStatus;

    public TaskDto(TaskEntity taskEntity) {
        id = taskEntity.getId();
        title = taskEntity.getTitle();
        description = taskEntity.getDescription();
        tasksStatus = taskEntity.getTaskStatus();
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
