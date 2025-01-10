package com.mindhub.todolist.exceptions;

public class TaskNotFoundExc extends RuntimeException {
    public TaskNotFoundExc(String message) {
        super(message);
    }
}
