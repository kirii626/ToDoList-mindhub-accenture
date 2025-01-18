package com.mindhub.todolist.exceptions;

public class InvalidTaskStatusExc extends RuntimeException {
    public InvalidTaskStatusExc(String message) {
        super(message);
    }
}
