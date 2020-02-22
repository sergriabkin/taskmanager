package com.taskmanager.service;

import com.taskmanager.model.Task;

import java.util.List;

public interface TaskService {

    public Task addTask(Task task);

    public List<Task> getAll();

    public Task getOne(long id);

    public Task updateTask(Task requestTask, long id);

    public void deleteTask(long id);
}
