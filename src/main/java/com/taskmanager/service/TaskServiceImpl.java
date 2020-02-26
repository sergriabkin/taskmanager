package com.taskmanager.service;

import com.taskmanager.aspect.Loggable;
import com.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.utils.TaskFilterUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;
    private final TaskFilterUtil taskFilter;

    public TaskServiceImpl(TaskRepository repository, TaskFilterUtil taskFilter) {
        this.repository = repository;
        this.taskFilter = taskFilter;
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
        Page<Task> allPageTasks = repository.findAll(pageable);
        List<Task> tasksFiltered = taskFilter.filterPageTasks(allPageTasks, predicate);
        return new PageImpl<>(tasksFiltered, pageable, allPageTasks.getTotalElements());
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
