package com.mindhub.todolist.repositories;

import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.models.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByTaskStatus(TaskStatus status);

    boolean existsByTitle(String title);

    Long countByUserEntityId(Long userId);

    List<TaskEntity> findByTitleOrderByIdAsc(String title);

    void deleteByTaskStatus(TaskStatus status);

    boolean existsByTaskStatus(TaskStatus taskStatus);

    List<TaskEntity> findByUserEntity(UserEntity userEntity);

    Optional<TaskEntity> findByIdAndUserEntity(Long id, UserEntity userEntity);
}
