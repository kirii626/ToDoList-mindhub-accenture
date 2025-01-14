package com.mindhub.todolist.controllers.admin;

import com.mindhub.todolist.dtos.NewUsuarioDto;
import com.mindhub.todolist.dtos.TaskDto;
import com.mindhub.todolist.dtos.UsuarioDto;
import com.mindhub.todolist.models.enums.RoleName;
import com.mindhub.todolist.services.TaskService;
import com.mindhub.todolist.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    @Autowired
    private UsuarioService usuarioService;


    @Operation(summary = "Assign role to a user", description = "Assigns a specific role to a user by user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role assigned successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid role provided")
    })
    @PutMapping("/{userId}/assign-role")
    public ResponseEntity<UsuarioDto> assignRoleToUser(@PathVariable Long userId,
                                                       @RequestParam RoleName roleName) {
        UsuarioDto updatedUsuario = usuarioService.assignRoleToUsuario(userId, roleName);
        return ResponseEntity.ok(updatedUsuario);
    }

    @Operation(summary = "Retrieve all users", description = "Fetches a list of all users in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all-users")
    public List<UsuarioDto> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    @Operation(summary = "Delete user by ID", description = "Deletes a user by their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get user by email", description = "Retrieves user details by email address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user-by-email/{email}")
    public UsuarioDto getUsuarioByEmail(@PathVariable String email) {
        return usuarioService.getUsuarioByEmail(email);
    }

    @Operation(summary = "Check if user exists by username", description = "Checks if a user exists with a given username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User existence checked successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/exists-user/{username}")
    public Boolean getExistsByUsername(@PathVariable String username) {
        return usuarioService.getExistByUsername(username);
    }

    @Operation(summary = "Count users by email", description = "Counts the number of users registered with a specific email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User count retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No users found with the given email")
    })
    @GetMapping("/count-users/{email}")
    public Long getCountByEmail(@PathVariable String email) {
        return usuarioService.getCountByEmail(email);
    }

    @Operation(summary = "Delete user by email", description = "Deletes a user using their email address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/delete-by-email/{email}")
    public void deleteByEmail(@PathVariable String email) {
        usuarioService.deleteByEmail(email);
    }

    @Operation(summary = "Create a new user", description = "Registers a new user with the provided data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user data provided"),
            @ApiResponse(responseCode = "409", description = "User already exists with the given email")
    })
    @PostMapping("/create-user")
    public ResponseEntity<UsuarioDto> createUsuario(@RequestBody NewUsuarioDto newUsuarioDto) {
        UsuarioDto newUsuario =  usuarioService.createUsuario(newUsuarioDto);
        return ResponseEntity
                .status(201)
                .body(newUsuario);
    }

}
