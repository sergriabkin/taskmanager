package com.taskmanager.utils;

import com.taskmanager.model.Task;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface TaskFilterUtil {
    default List<Task> filterPageTasks(Page<Task> allPageTasks, Predicate<Task> predicate){
        return allPageTasks.get()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}
