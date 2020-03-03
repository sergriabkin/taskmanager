package com.taskmanager.web;

import com.taskmanager.aspect.Loggable;
import com.taskmanager.model.Task;
import com.taskmanager.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TaskApiController {

    private final TaskService service;

    public TaskApiController(TaskService service) {
        this.service = service;
    }

    @Loggable
    @PostMapping("/tasks")
    @PreAuthorize("hasRole('ROLE_CSR')")
    public Task addTask(@RequestBody Task task) {
        return service.addTask(task);
    }

    @Loggable
    @GetMapping("/tasks")
    public Page<Task> getAll(Pageable pageable) {
        return service.getAll(pageable);
    }

    @Loggable
    @GetMapping("/tasks/urgent")
    public Page<Task> getAllUrgent(Pageable pageable) {
        return service.getAllUrgent(pageable);
    }

    @Loggable
    @GetMapping("/tasks/{id}")
    public Task getOne(@PathVariable long id) {
        return service.getOne(id);
    }

    @Loggable
    @PutMapping("/tasks/{id}")
    @PreAuthorize("hasRole('ROLE_CSR')")
    public Task updateTask(@RequestBody Task requestTask, @PathVariable long id) {
        return service.updateTask(requestTask, id);
    }

    @Loggable
    @DeleteMapping("/tasks/{id}")
    @PreAuthorize("hasRole('ROLE_CSR')")
    public void deleteTask(@PathVariable long id) {
        service.deleteTask(id);
    }

}
