package com.mindhub.todolist;

import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.models.Usuario;
import com.mindhub.todolist.models.enums.TaskStatus;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TodolistApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodolistApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(UsuarioRepository usuarioRepository,
									  TaskRepository taskRepository) {

		return args -> {
			Usuario usuario = new Usuario("kiara", "lsdwo","sdksd@gmail.com");
			usuarioRepository.save(usuario);
			Task task = new Task("ir al gym","hacer la rutina",TaskStatus.COMPLETED);
			usuario.addTask(task);
			taskRepository.save(task);
		};
	}
}
