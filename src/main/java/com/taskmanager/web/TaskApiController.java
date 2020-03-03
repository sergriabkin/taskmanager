package com.taskmanager.web;

import com.taskmanager.aspect.Loggable;
import com.taskmanager.dto.TaskDto;
import com.taskmanager.model.Task;
import com.taskmanager.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TaskApiController {

    private final TaskService service;
    private final TaskAssembler assembler;
    private final PagedResourcesAssembler<Task> pagedAssembler;

    public TaskApiController(TaskService service, TaskAssembler assembler, PagedResourcesAssembler<Task> pagedResourcesAssembler) {
        this.service = service;
        this.assembler = assembler;
        this.pagedAssembler = pagedResourcesAssembler;
    }

    @Loggable
    @PostMapping("/tasks")
    @PreAuthorize("hasRole('ROLE_CSR')")
    public TaskDto addTask(@RequestBody TaskDto taskDto) {
        Task task = service.addTask(taskDto.toTask());
        return assembler.toModel(task);
    }

    @Loggable
    @GetMapping("/tasks")
    public PagedModel<TaskDto> getAll(Pageable pageable) {
        Page<Task> page = service.getAll(pageable);
        return pagedAssembler.toModel(page, assembler);
    }

    @Loggable
    @GetMapping("/tasks/urgent")
    public PagedModel<TaskDto> getAllUrgent(Pageable pageable) {
        Page<Task> page = service.getAllUrgent(pageable);
        return pagedAssembler.toModel(page, assembler);
    }

    @Loggable
    @GetMapping("/tasks/{id}")
    public TaskDto getOne(@PathVariable long id) {
        return assembler.toModel(service.getOne(id));
    }

    @Loggable
    @PutMapping("/tasks/{id}")
    @PreAuthorize("hasRole('ROLE_CSR')")
    public TaskDto updateTask(@RequestBody TaskDto taskDto, @PathVariable long id) {
        Task task = service.updateTask(taskDto.toTask(), id);
        return assembler.toModel(task);
    }

    @Loggable
    @DeleteMapping("/tasks/{id}")
    @PreAuthorize("hasRole('ROLE_CSR')")
    public void deleteTask(@PathVariable long id) {
        service.deleteTask(id);
    }

}
