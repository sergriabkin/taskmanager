package com.taskmanager.service;

import com.taskmanager.aspect.Loggable;
import com.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;

    public TaskServiceImpl(TaskRepository repository) {
        this.repository = repository;
    }

    public Task addTask(Task task) {
        return repository.save(task);
    }

    @Loggable
    public List<Task> getAll() {
        return repository.findAll();
    }

    @Loggable
    public Task getOne(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Loggable
    public Task updateTask(Task requestTask, long id) {
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
    public void deleteTask(long id) {
        repository.deleteById(id);
    }

}
