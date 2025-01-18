package com.mindhub.todolist.services.implementations;

import com.mindhub.todolist.dtos.NewUserDto;
import com.mindhub.todolist.dtos.UserDto;
import com.mindhub.todolist.exceptions.UserAlreadyExistsExc;
import com.mindhub.todolist.exceptions.UserNotFoundExc;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.models.enums.RoleName;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    private UserEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundExc("User not found by email: " + email));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserEntityById(Long id) {
    UserEntity userEntity = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundExc("User not found by ID: " + id));
    return new UserDto(userEntity);
    }

    @Override
    public UserDto createUserEntity(NewUserDto newUserDto) {
        if (userRepository.findByEmail(newUserDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsExc("The email already exists: " + newUserDto.getEmail());
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(newUserDto.getUsername());
        userEntity.setEmail(newUserDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(newUserDto.getPassword()));

        UserEntity savedUser = userRepository.save(userEntity);
        return new UserDto(savedUser);
    }

    @Override
    public void deleteUserEntity(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundExc("User not found by ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDto updateUserEntity(Long id, UserDto userDto) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundExc("User not found by ID: " + id));

        userEntity.setUsername(userDto.getUsername());
        userEntity.setEmail(userDto.getEmail());

        UserEntity updatedUser = userRepository.save(userEntity);

        return new UserDto(updatedUser);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundExc("User not found by email: " + email));
        return new UserDto(userEntity);
    }

    @Override
    public boolean getExistByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Long getCountByEmail(String email) {
        return userRepository.countByEmail(email);
    }

    @Override
    @Transactional
    public void deleteByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundExc("User not found by email: " + email));
        userRepository.delete(userEntity);
    }

    @Override
    @Transactional
    public UserDto assignRoleToUser(Long id, RoleName roleName) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundExc("User not found by ID: "+id));

        userEntity.setRoleName(roleName);
        UserEntity updatedUser = userRepository.save(userEntity);
        return new UserDto(updatedUser);
    }

    @Override
    public UserDto getAuthenticatedUserDto() {
        UserEntity userEntity = getAuthenticatedUser();
        return new UserDto(userEntity);
    }


}
