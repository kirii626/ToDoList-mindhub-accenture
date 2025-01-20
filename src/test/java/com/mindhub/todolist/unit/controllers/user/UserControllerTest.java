package com.mindhub.todolist.unit.controllers.user;

import com.mindhub.todolist.controllers.user.UserController;
import com.mindhub.todolist.dtos.UserDto;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Simulated user entity
        userEntity = new UserEntity();
        userEntity.setUsername("testuser");
        userEntity.setEmail("testuser@email.com");
    }

    @Test
    void getCurrentUserInfoSuccess() {
        // Mock the service call
        when(userService.getAuthenticatedUserDto()).thenReturn(new UserDto(userEntity));

        // Call the controller method
        ResponseEntity<UserDto> response = userController.getCurrentUserInfo();

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("testuser", response.getBody().getUsername());
        assertEquals("testuser@email.com", response.getBody().getEmail());

        // Verify interaction with the service
        verify(userService, times(1)).getAuthenticatedUserDto();
    }

    @Test
    void getCurrentUserInfoUnauthorized() {
        // Mock the service call to throw an exception
        when(userService.getAuthenticatedUserDto()).thenThrow(new RuntimeException("Unauthorized"));

        // Call the controller method and expect an exception
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userController.getCurrentUserInfo();
        });

        // Assertions
        assertEquals("Unauthorized", exception.getMessage());

        // Verify interaction with the service
        verify(userService, times(1)).getAuthenticatedUserDto();
    }

}
