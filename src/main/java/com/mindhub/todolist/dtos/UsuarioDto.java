package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.Usuario;

public class UsuarioDto {


    private String username, email;

    public UsuarioDto() {
    }

    public UsuarioDto(Usuario usuario) {
        username = usuario.getUsername();
        email = usuario.getEmail();
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

}
