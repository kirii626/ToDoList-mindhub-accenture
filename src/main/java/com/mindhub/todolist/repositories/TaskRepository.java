package com.mindhub.todolist.repositories;

import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.models.Usuario;
import com.mindhub.todolist.models.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByTaskStatus(TaskStatus status);

    boolean existsByTitle(String title);

    Long countByUsuarioId(Long usuarioId);

    List<Task> findByTitleOrderByIdAsc(String title);

    void deleteByTaskStatus(TaskStatus status);

    boolean existsByTaskStatus(TaskStatus taskStatus);

    List<Task> findByUsuario(Usuario usuario);

    Optional<Task> findByIdAndUsuario(Long id, Usuario usuario);
}
