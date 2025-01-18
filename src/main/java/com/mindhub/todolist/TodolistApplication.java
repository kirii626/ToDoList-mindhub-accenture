package com.mindhub.todolist;

import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.models.enums.RoleName;
import com.mindhub.todolist.models.enums.TaskStatus;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TodolistApplication {

	/*
	* @Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(TodolistApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(UserRepository userRepository,
									  TaskRepository taskRepository) {

		return args -> {
			UserEntity userEntity = new UserEntity("kiara", "aaa@gmail.com", RoleName.ADMIN,passwordEncoder.encode("12345"));
			userRepository.save(userEntity);
			TaskEntity taskEntity = new TaskEntity("ir al gym","hacer la rutina",TaskStatus.COMPLETED);
			userEntity.addTask(taskEntity);
			taskRepository.save(taskEntity);
		};
	}

	* */


}
