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

@Service
public class TaskServiceDemo implements TaskService {

    public static final int URGENT_PRIORITY = 5;
    private final TaskRepository repository;
    private final TaskFilterUtil taskFilter;

    public TaskServiceDemo(TaskRepository repository) {
        this.repository = repository;
        taskFilter = new TaskFilterUtil();
    }

    public Task addTask(Task task) {
        return repository.save(task);
    }

    @Loggable
    public Page<Task> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Loggable
    public Page<Task> getAllUrgent(Pageable pageable) {
        Page<Task> allPageTasks = repository.findAll(pageable);
        List<Task> tasksFiltered = taskFilter.filterPageTasks(allPageTasks, this::isTaskUrgent);
        return new PageImpl<>(tasksFiltered, pageable, allPageTasks.getTotalElements());
    }

    private boolean isTaskUrgent(Task task) {
        return task.getPriority() >= URGENT_PRIORITY;
    }

    @Loggable
    public Task getOne(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Loggable
    public Task updateTask(Task requestTask, long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        }
        return repository.save(requestTask);
    }

    @Loggable
    public void deleteTask(long id) {
        repository.deleteById(id);
    }

}
