package com.mindhub.todolist.models;

import com.mindhub.todolist.models.enums.RoleName;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleName roleName = RoleName.USER;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaskEntity> tasks = new HashSet<>();

    public UserEntity() {
    }

    public UserEntity(String username, String email, RoleName roleName, String password) {
        this.username = username;
        this.email = email;
        this.roleName = roleName;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RoleName getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }

    public void addTask(TaskEntity taskEntity) {
        taskEntity.setUserEntity(this);
        tasks.add(taskEntity);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", roleName=" + roleName +
                '}';
    }
}
