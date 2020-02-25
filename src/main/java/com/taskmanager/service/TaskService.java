package com.taskmanager.service;

import com.taskmanager.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Predicate;

public interface TaskService {

    Task addTask(Task task);

    List<Task> getAll();

    Page<Task> getAll(Pageable pageable);

    Page<Task> getAllFiltered(Pageable pageable, Predicate<Task> predicate);

    Task getOne(long id);

    Task updateTask(Task requestTask, long id);

    void deleteTask(long id);
}
