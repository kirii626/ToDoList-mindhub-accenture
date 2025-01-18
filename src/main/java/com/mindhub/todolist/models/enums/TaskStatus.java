package com.mindhub.todolist.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.mindhub.todolist.exceptions.InvalidTaskStatusExc;

public enum TaskStatus {

    PENDING,
    IN_PROGRESS,
    COMPLETED;

    @JsonCreator
    public static TaskStatus fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Task Status cannot be null or empty. Allowed values: PENDING, IN_PROGRESS, COMPLETED");
        }
        try {
            return TaskStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Task Status: '" + value + "'. Allowed values: PENDING, IN_PROGRESS, COMPLETED");
        }
    }
}
