package com.taskmanager.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(long id) {
        super("Could not find task with id: " + id);
    }

}
