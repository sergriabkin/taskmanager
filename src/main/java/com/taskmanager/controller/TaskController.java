package com.taskmanager.controller;

import com.taskmanager.aspect.Loggable;
import com.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    private final TaskRepository repository;

    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @Loggable
    @PostMapping("/tasks")
    public Task addTask(@RequestBody Task task) {
        return repository.save(task);
    }

    @Loggable
    @GetMapping("/tasks")
    public List<Task> getAll() {
        return repository.findAll();
    }

    @Loggable
    @GetMapping("/tasks/{id}")
    public Task getOne(@PathVariable long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Loggable
    @PutMapping("/tasks/{id}")
    public Task updateTask(@RequestBody Task requestTask, @PathVariable long id){
        return repository.findById(id)
                .map(task -> replaceTask(requestTask, id))
                .orElse(addNewTask(requestTask));
    }

    private Task replaceTask(Task requestTask, long id) {
        repository.deleteById(id);
        return addTask(requestTask);
    }

    private Task addNewTask(Task requestTask) {
        return repository.save(requestTask);
    }

    @Loggable
    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable long id){
        repository.deleteById(id);
    }

}
