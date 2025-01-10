package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.NewUsuarioDto;
import com.mindhub.todolist.dtos.UsuarioDto;
import com.mindhub.todolist.repositories.UsuarioRepository;
import com.mindhub.todolist.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioDto> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    @GetMapping("/{id}")
    public UsuarioDto getUsuarioById(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id);
    }

    @PostMapping
    public UsuarioDto createUsuario(@RequestBody NewUsuarioDto newUsuarioDto) {
        return usuarioService.createUsuario(newUsuarioDto);
    }

    @PutMapping("/{id}")
    public UsuarioDto updateUsuario(@PathVariable Long id, @RequestBody UsuarioDto usuarioDto) {
        return usuarioService.updateUsuario(id, usuarioDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-email/{email}")
    public UsuarioDto getUsuarioByEmail(@PathVariable String email) {
        return usuarioService.getUsuarioByEmail(email);
    }

    @GetMapping("/exists/{username}")
    public Boolean getExistsByUsername(@PathVariable String username) {
        return usuarioService.getExistByUsername(username);
    }

    @GetMapping("/count/{email}")
    public Long getCountByEmail(@PathVariable String email) {
        return usuarioService.getCountByEmail(email);
    }

    @DeleteMapping("/delete/{email}")
    public void deleteByEmail(@PathVariable String email) {
        usuarioService.deleteByEmail(email);
    }
}
