package com.mindhub.todolist.services.implementations;

import com.mindhub.todolist.dtos.NewUsuarioDto;
import com.mindhub.todolist.dtos.UsuarioDto;
import com.mindhub.todolist.exceptions.UserAlreadyExistsExc;
import com.mindhub.todolist.exceptions.UserNotFoundExc;
import com.mindhub.todolist.models.Usuario;
import com.mindhub.todolist.models.enums.RoleName;
import com.mindhub.todolist.repositories.UsuarioRepository;
import com.mindhub.todolist.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;


    private Usuario getAuthenticatedUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundExc("User not found by email: " + email));
    }

    @Override
    public List<UsuarioDto> getAllUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioDto getUsuarioById(Long id) {
    Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundExc("User not found by ID: " + id));
    return new UsuarioDto(usuario);
    }

    @Override
    public UsuarioDto createUsuario(NewUsuarioDto newUsuarioDto) {
        if (usuarioRepository.findByEmail(newUsuarioDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsExc("The email already exists: " + newUsuarioDto.getEmail());
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(newUsuarioDto.getUsername());
        usuario.setEmail(newUsuarioDto.getEmail());
        usuario.setPassword(passwordEncoder.encode(newUsuarioDto.getPassword()));

        Usuario savedUsuario = usuarioRepository.save(usuario);
        return new UsuarioDto(savedUsuario);
    }

    @Override
    public void deleteUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UserNotFoundExc("User not found by ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public UsuarioDto updateUsuario(Long id, UsuarioDto usuarioDto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundExc("User not found by ID: " + id));

        usuario.setUsername(usuarioDto.getUsername());
        usuario.setEmail(usuarioDto.getEmail());

        Usuario updatedUsuario = usuarioRepository.save(usuario);

        return new UsuarioDto(updatedUsuario);
    }

    @Override
    public UsuarioDto getUsuarioByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundExc("User not found by email: " + email));
        return new UsuarioDto(usuario);
    }

    @Override
    public boolean getExistByUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    @Override
    public Long getCountByEmail(String email) {
        return usuarioRepository.countByEmail(email);
    }

    @Override
    @Transactional
    public void deleteByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundExc("User not found by email: " + email));
        usuarioRepository.delete(usuario);
    }

    @Override
    @Transactional
    public UsuarioDto assignRoleToUsuario(Long id, RoleName roleName) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundExc("User not found by ID: "+id));

        usuario.setRoleName(roleName);
        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return new UsuarioDto(updatedUsuario);
    }

    @Override
    public UsuarioDto getAuthenticatedUsuarioDto() {
        Usuario usuario = getAuthenticatedUsuario();
        return new UsuarioDto(usuario);
    }


}
