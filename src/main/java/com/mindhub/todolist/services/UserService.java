package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.NewUserDto;
import com.mindhub.todolist.dtos.UserDto;
import com.mindhub.todolist.models.enums.RoleName;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto getUserEntityById(Long id);

    UserDto createUserEntity(NewUserDto newUserDto);

    void deleteUserEntity(Long id);

    UserDto updateUserEntity(Long id, UserDto userDto);

    UserDto getUserByEmail(String email);

    boolean getExistByUsername(String username);

    Long getCountByEmail(String email);

    void deleteByEmail(String email);

    UserDto assignRoleToUser(Long id, RoleName roleName);

    UserDto getAuthenticatedUserDto();
}
