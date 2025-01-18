package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.models.enums.RoleName;

public class UserDto {

    private String username, email;

    private RoleName roleName;

    public UserDto() {
    }

    public UserDto(UserEntity userEntity) {
        username = userEntity.getUsername();
        email = userEntity.getEmail();
        roleName = userEntity.getRoleName();
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public RoleName getRoleName() {
        return roleName;
    }
}
