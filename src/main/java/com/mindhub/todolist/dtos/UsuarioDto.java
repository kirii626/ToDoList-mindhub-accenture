package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.Usuario;

public class UsuarioDto {

    private Long id;

    private String username, email;

    public UsuarioDto() {
    }

    public UsuarioDto(Usuario usuario) {
        id = usuario.getId();
        username = usuario.getUsername();
        email = usuario.getEmail();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

}
