package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.NewUsuarioDto;
import com.mindhub.todolist.dtos.UsuarioDto;
import com.mindhub.todolist.models.Usuario;
import com.mindhub.todolist.models.enums.RoleName;

import java.util.List;

public interface UsuarioService {

    List<UsuarioDto> getAllUsuarios();

    UsuarioDto getUsuarioById(Long id);

    UsuarioDto createUsuario(NewUsuarioDto newUsuarioDto);

    void deleteUsuario(Long id);

    UsuarioDto updateUsuario(Long id, UsuarioDto usuarioDto);

    UsuarioDto getUsuarioByEmail(String email);

    boolean getExistByUsername(String username);

    Long getCountByEmail(String email);

    void deleteByEmail(String email);

    UsuarioDto assignRoleToUsuario(Long id, RoleName roleName);

    UsuarioDto getAuthenticatedUsuarioDto();
}
