package com.mindhub.todolist.repositories;

import com.mindhub.todolist.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByUsername(String username);

    long countByEmail(String email);

    void deleteByEmail(String email);

}
