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
public class LoadDatabaseConfiguration {
    @Bean
    CommandLineRunner initDatabase(TaskRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new Task("Wake up", "", 5)));
            log.info("Preloading " + repository.save(new Task("Brush the teeth", "", 4)));
        };
    }
}
