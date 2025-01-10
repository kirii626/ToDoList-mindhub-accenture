package com.mindhub.todolist.dtos;


public class NewUsuarioDto {

    private String username;
    private String email;

    public NewUsuarioDto() {
    }

    public NewUsuarioDto(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
