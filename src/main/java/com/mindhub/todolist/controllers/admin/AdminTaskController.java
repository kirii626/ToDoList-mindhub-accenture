package com.mindhub.todolist.controllers.admin;

import com.mindhub.todolist.dtos.TaskDto;
import com.mindhub.todolist.dtos.TaskInputDto;
import com.mindhub.todolist.models.enums.TaskStatus;
import com.mindhub.todolist.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tasks")
public class AdminTaskController {

    @Autowired
    private TaskService taskService;

    @Operation(summary = "Retrieve all tasks", description = "Fetches all tasks available in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all-tasks")
    public List<TaskDto> getAllTasks() {
        return taskService.getAllTasks();
    }

    @Operation(summary = "Retrieve a task by ID", description = "Fetches a specific task by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/task-by-id/{id}")
    public TaskDto getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }


    @Operation(summary = "Create a new task", description = "Creates a new task with the provided information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public TaskDto createTask(@RequestBody TaskInputDto taskInputDto) {
        return taskService.createTask(taskInputDto);
    }

    @Operation(summary = "Update an existing task", description = "Updates the details of an existing task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{id}")
    public TaskDto updateTask(@PathVariable Long id, @RequestBody TaskInputDto taskInputDto) {
        return taskService.updateTask(id, taskInputDto);
    }

    @Operation(summary = "Delete a task by ID", description = "Deletes a task using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @Operation(summary = "Get tasks by status", description = "Retrieves tasks that match a specific status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No tasks found with the given status")
    })
    @GetMapping("/by-status/{taskStatus}")
    public List<TaskDto> getFindByTaskStatus(@PathVariable TaskStatus taskStatus) {
        return taskService.getFindByTaskStatus(taskStatus);
    }

    @Operation(summary = "Check if a task exists by title", description = "Checks if a task exists with the given title.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check completed successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/by-title/{title}")
    public boolean getExistsByTitle(@PathVariable String title) {
        return taskService.getExistsByTitle(title);
    }

    @Operation(summary = "Count tasks by user ID", description = "Counts the number of tasks associated with a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/count/{userId}")
    public Long getCountByUserId(@PathVariable Long userId) {
        return taskService.getCountByUserEntityId(userId);
    }

    @Operation(summary = "Filter tasks by title", description = "Retrieves tasks that match a specific title, ordered by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No tasks found with the given title")
    })
    @GetMapping("/filter/{title}")
    public List<TaskDto> getfindByTitleOrderByIdAsc(@PathVariable String title) {
        return taskService.getfindByTitleOrderByIdAsc(title);
    }

    @Operation(summary = "Delete tasks by status", description = "Deletes all tasks that match a specific status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tasks deleted successfully"),
            @ApiResponse(responseCode = "404", description = "No tasks found with the given status")
    })
    @DeleteMapping("/delete/{status}")
    public void deleteByTaskStatus(@PathVariable TaskStatus status) {
        taskService.deleteByTaskStatus(status);
    }


}
