package com.mindhub.todolist.controllers.user;

import com.mindhub.todolist.dtos.TaskDto;
import com.mindhub.todolist.dtos.TaskInputDto;
import com.mindhub.todolist.dtos.TaskInputDtoForUser;
import com.mindhub.todolist.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Operation(summary = "Get all tasks for current user", description = "Retrieves a list of all tasks associated with the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasksForCurrentUser());
    }

    @Operation(summary = "Get task by ID", description = "Fetches a specific task by its ID, ensuring it belongs to the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found or does not belong to user"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getUserTaskById(id));
    }

    @Operation(summary = "Create a task", description = "Creates a new task for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid task data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PostMapping("/create")
    public ResponseEntity<TaskDto> createTaskForCurrentUser(@Valid @RequestBody TaskInputDtoForUser taskInputDtoForUser) {
        TaskDto createdTask = taskService.createTaskForCurrentUser(taskInputDtoForUser);
        return ResponseEntity.ok(createdTask);

    }

    @Operation(summary = "Update a task", description = "Updates an existing task by ID for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found or does not belong to user"),
            @ApiResponse(responseCode = "400", description = "Invalid task data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @RequestBody TaskInputDtoForUser taskInputDtoForUser) {
        return ResponseEntity.ok(taskService.updateUserTask(id, taskInputDtoForUser));
    }

    @Operation(summary = "Delete a task", description = "Deletes a task by its ID, ensuring it belongs to the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found or does not belong to user"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteUserTask(id);
        return ResponseEntity.noContent().build();
    }
}
