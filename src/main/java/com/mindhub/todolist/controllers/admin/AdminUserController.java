package com.mindhub.todolist.controllers.admin;

import com.mindhub.todolist.dtos.NewUserDto;
import com.mindhub.todolist.dtos.UserDto;
import com.mindhub.todolist.models.enums.RoleName;
import com.mindhub.todolist.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;


    @Operation(summary = "Assign role to a user", description = "Assigns a specific role to a user by user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role assigned successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid role provided")
    })
    @PutMapping("/{userId}/assign-role")
    public ResponseEntity<UserDto> assignRoleToUser(@PathVariable Long userId,
                                                    @RequestParam RoleName roleName) {
        UserDto updatedUser = userService.assignRoleToUser(userId, roleName);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Retrieve all users", description = "Fetches a list of all users in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all-users")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Delete user by ID", description = "Deletes a user by their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserEntity(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get user by email", description = "Retrieves user details by email address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user-by-email/{email}")
    public UserDto getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @Operation(summary = "Check if user exists by username", description = "Checks if a user exists with a given username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User existence checked successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/exists-user/{username}")
    public Boolean getExistsByUsername(@PathVariable String username) {
        return userService.getExistByUsername(username);
    }

    @Operation(summary = "Count users by email", description = "Counts the number of users registered with a specific email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User count retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No users found with the given email")
    })
    @GetMapping("/count-users/{email}")
    public Long getCountByEmail(@PathVariable String email) {
        return userService.getCountByEmail(email);
    }

    @Operation(summary = "Delete user by email", description = "Deletes a user using their email address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/delete-by-email/{email}")
    public void deleteByEmail(@PathVariable String email) {
        userService.deleteByEmail(email);
    }

    @Operation(summary = "Create a new user", description = "Registers a new user with the provided data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user data provided"),
            @ApiResponse(responseCode = "409", description = "User already exists with the given email")
    })
    @PostMapping("/create-user")
    public ResponseEntity<UserDto> createUser(@RequestBody NewUserDto newUserDto) {
        UserDto newUser =  userService.createUserEntity(newUserDto);
        return ResponseEntity
                .status(201)
                .body(newUser);
    }

}
