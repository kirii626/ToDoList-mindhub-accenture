package com.mindhub.todolist.models;

import com.mindhub.todolist.models.enums.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "The title can't be null")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "The description can't be null")
    @Column(nullable = false)
    private String description;

    @NotBlank(message = "The task status can´t be null, try PENDING, IN_PROGRESS OR COMPLETED ")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus taskStatus;

    @ManyToOne
    private Usuario usuario;

    public Task() {
    }

    public Task(String title, String description, TaskStatus taskStatus) {
        this.title = title;
        this.description = description;
        this.taskStatus = taskStatus;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
