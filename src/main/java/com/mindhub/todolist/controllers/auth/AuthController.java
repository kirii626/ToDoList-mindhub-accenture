package com.mindhub.todolist.controllers.auth;

import com.mindhub.todolist.config.JwtUtils;
import com.mindhub.todolist.dtos.LoginUser;
import com.mindhub.todolist.dtos.NewUserDto;
import com.mindhub.todolist.dtos.UserDto;
import com.mindhub.todolist.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtil;

    @Operation(summary = "Register a new user", description = "Allows a new user to register in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "409", description = "User already exists with provided email")
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody NewUserDto newUserDto) {
        UserDto createdUser = userService.createUserEntity(newUserDto);
        return ResponseEntity.ok(createdUser);

    }


    @Operation(summary = "Authenticate user and generate JWT", description = "Allows a user to log in and receive a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "409", description = "Invalid email or password"),
            @ApiResponse(responseCode = "400", description = "Invalid request format")
    })
    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginUser loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(authentication.getName());
        return ResponseEntity.ok(jwt);
    }


}
