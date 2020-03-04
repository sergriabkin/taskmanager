package com.taskmanager.repository;

import com.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "tasks", path = "tasks")
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByTitle(@Param("title") String title);

    @Override
    @RestResource(exported = false)
    <S extends Task> S save(S task);

    @Override
    @RestResource(exported = false)
    void deleteById(Long id);
}
