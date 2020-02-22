package com.taskmanager.controller;

import com.taskmanager.aspect.Loggable;
import com.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.service.TaskService;
import com.taskmanager.service.TaskServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @Loggable
    @PostMapping("/tasks")
    public Task addTask(@RequestBody Task task) {
        return service.addTask(task);
    }

    @Loggable
    @GetMapping("/tasks")
    public List<Task> getAll() {
        return service.getAll();
    }

    @Loggable
    @GetMapping("/tasks/{id}")
    public Task getOne(@PathVariable long id) {
        return service.getOne(id);
    }

    @Loggable
    @PutMapping("/tasks/{id}")
    public Task updateTask(@RequestBody Task requestTask, @PathVariable long id){
        return service.updateTask(requestTask, id);
    }

    @Loggable
    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable long id){
        service.deleteTask(id);
    }

}
