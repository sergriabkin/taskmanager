package com.taskmanager.service;

import com.taskmanager.aspect.Loggable;
import com.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public Page<Task> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Loggable
    public Page<Task> getAllFiltered(Pageable pageable, Predicate<Task> predicate) {
        Page<Task> all = repository.findAll(pageable);
        return new PageImpl<>(
                all.get()
                        .filter(predicate)
                        .collect(Collectors.toList()),
                pageable,
                all.getTotalElements()
        );
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
