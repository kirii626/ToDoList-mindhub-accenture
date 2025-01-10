package com.mindhub.todolist.exceptions;

public class UserAlreadyExistsExc extends RuntimeException {
    public UserAlreadyExistsExc(String message) {
        super(message);
    }
}
