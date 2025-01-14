package com.mindhub.todolist.models;

import com.mindhub.todolist.models.enums.RoleName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The username can´t be null")
    @Column(nullable = false)
    private String username;

    @NotBlank(message = "The password can´t be null")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "The email can´t be null")
    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleName roleName = RoleName.USER;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> tasks = new HashSet<>();

    public Usuario() {
    }

    public Usuario(String username, String email, RoleName roleName, String password) {
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

    public void addTask(Task task) {
        task.setUsuario(this);
        tasks.add(task);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", roleName=" + roleName +
                '}';
    }
}
