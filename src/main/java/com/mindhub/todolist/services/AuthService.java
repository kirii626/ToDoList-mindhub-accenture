package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.NewUserDto;
import com.mindhub.todolist.dtos.UserDto;
import jakarta.validation.Valid;

public interface AuthService {

    String authenticateAndGenerateToken(String email, String password);

    UserDto registerNewUser(@Valid NewUserDto newUserDto);
}
