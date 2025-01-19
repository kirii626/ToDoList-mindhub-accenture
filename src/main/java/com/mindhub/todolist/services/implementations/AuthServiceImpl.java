package com.mindhub.todolist.services.implementations;

import com.mindhub.todolist.config.JwtUtils;
import com.mindhub.todolist.dtos.NewUserDto;
import com.mindhub.todolist.dtos.UserDto;
import com.mindhub.todolist.services.AuthService;
import com.mindhub.todolist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    public String authenticateAndGenerateToken(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String username = authentication.getName();

        String role = authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(authority -> authority.getAuthority())
                .orElse("USER");  // Valor predeterminado si no hay roles

        return jwtUtil.generateToken(username);
    }

    @Override
    public UserDto registerNewUser(NewUserDto newUserDto) {
        return userService.createUserEntity(newUserDto);
    }
}
