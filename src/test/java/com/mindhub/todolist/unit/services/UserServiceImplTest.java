package com.mindhub.todolist.unit.services;

import com.mindhub.todolist.dtos.NewUserDto;
import com.mindhub.todolist.dtos.UserDto;
import com.mindhub.todolist.exceptions.UserAlreadyExistsExc;
import com.mindhub.todolist.exceptions.UserNotFoundExc;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.models.enums.RoleName;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.implementations.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserEntity userEntity;
    private NewUserDto newUserDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Datos simulados
        userEntity = mock(UserEntity.class);
        when(userEntity.getUsername()).thenReturn("testuser");
        when(userEntity.getEmail()).thenReturn("testuser@email.com");
        when(userEntity.getPassword()).thenReturn("hashedpassword");
        when(userEntity.getRoleName()).thenReturn(RoleName.USER);
        when(userEntity.getId()).thenReturn(1L);

        newUserDto = new NewUserDto("testuser", "testuser@email.com", "password123");
    }

    @Test
    void createUserEntitySuccess() {
        when(userRepository.findByEmail(newUserDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(newUserDto.getPassword())).thenReturn("hashedpassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserDto createdUser = userService.createUserEntity(newUserDto);

        assertNotNull(createdUser);
        assertEquals("testuser", createdUser.getUsername());
        assertEquals(1L, createdUser.getId());
        verify(userRepository, times(1)).findByEmail(newUserDto.getEmail());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void createUserEntityEmailAlreadyExists() {
        when(userRepository.findByEmail(newUserDto.getEmail())).thenReturn(Optional.of(userEntity));

        assertThrows(UserAlreadyExistsExc.class, () -> userService.createUserEntity(newUserDto));
        verify(userRepository, times(1)).findByEmail(newUserDto.getEmail());
    }

    @Test
    void getUserEntityByIdSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        UserDto userDto = userService.getUserEntityById(1L);

        assertNotNull(userDto);
        assertEquals("testuser", userDto.getUsername());
        assertEquals(1L, userDto.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserEntityByIdUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundExc.class, () -> userService.getUserEntityById(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void deleteUserEntitySuccess() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUserEntity(1L);

        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUserEntityUserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundExc.class, () -> userService.deleteUserEntity(1L));
        verify(userRepository, times(1)).existsById(1L);
    }

    @Test
    void updateUserEntitySuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserDto updatedUser = userService.updateUserEntity(1L, new UserDto(userEntity));

        assertNotNull(updatedUser);
        assertEquals("testuser", updatedUser.getUsername());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void updateUserEntityUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundExc.class, () -> userService.updateUserEntity(1L, new UserDto(userEntity)));
        verify(userRepository, times(1)).findById(1L);
    }


    @Test
    void assignRoleToUserUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundExc.class, () -> userService.assignRoleToUser(1L, RoleName.ADMIN));
        verify(userRepository, times(1)).findById(1L);
    }
}
