package com.taskmanager;

import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.utils.TaskFilterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CommandLineRunnerConfiguration {
    @Bean
    CommandLineRunner checkDatabaseContent(TaskRepository repository) {
        return args -> {
            repository.findAll().forEach( task -> log.info("Task is present in DB: " + task));
        };
    }
}
