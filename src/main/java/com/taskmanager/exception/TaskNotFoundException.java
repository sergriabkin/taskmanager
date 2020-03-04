package com.taskmanager.exception;

public class TaskNotFoundException extends RuntimeException {

    public static final String TASK_NOT_FOUND_MESSAGE = "Could not find task with id: ";

    public TaskNotFoundException(long id) {
        super(TASK_NOT_FOUND_MESSAGE + id);
    }

}
