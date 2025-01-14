package com.mindhub.todolist.controllers.user;

import com.mindhub.todolist.dtos.NewUsuarioDto;
import com.mindhub.todolist.dtos.UsuarioDto;
import com.mindhub.todolist.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Get current user info",
            description = "Retrieves the profile information of the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User information retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated")
    })
    @GetMapping("/me")
    public ResponseEntity<UsuarioDto> getCurrentUserInfo() {
        return ResponseEntity.ok(usuarioService.getAuthenticatedUsuarioDto());
    }


}
