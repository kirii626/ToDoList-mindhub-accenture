package com.mindhub.todolist.controllers.user;

import com.mindhub.todolist.dtos.UserDto;
import com.mindhub.todolist.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Get current user info",
            description = "Retrieves the profile information of the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User information retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated")
    })
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUserInfo() {
        return ResponseEntity.ok(userService.getAuthenticatedUserDto());
    }


}
