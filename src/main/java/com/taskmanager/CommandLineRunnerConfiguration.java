package com.taskmanager;

import com.taskmanager.repository.RoleRepository;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CommandLineRunnerConfiguration {
    @Bean
    CommandLineRunner checkDatabaseContent(TaskRepository taskRepository, UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            taskRepository.findAll().forEach(task -> log.info("Task is present in DB: " + task));
            userRepository.findAll().forEach(user -> log.info("User is present in DB: " + user));
            roleRepository.findAll().forEach(role -> log.info("Role is present in DB: " + role));
        };
    }
}
