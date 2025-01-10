package com.mindhub.todolist.exceptions;

public class UserNotFoundExc extends RuntimeException {
    public UserNotFoundExc(String message) {
        super(message);
    }
}
