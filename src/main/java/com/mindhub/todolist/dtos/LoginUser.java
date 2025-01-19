package com.mindhub.todolist.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginUser(@NotBlank(message = "Email is required")
                        @Email(message = "Invalid email format")
                        String email,

                        @NotBlank(message = "Password is required")
                        String password) {
}
