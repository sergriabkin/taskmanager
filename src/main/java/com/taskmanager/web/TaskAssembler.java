package com.taskmanager.web;

import com.taskmanager.dto.TaskDto;
import com.taskmanager.model.Task;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class TaskAssembler extends RepresentationModelAssemblerSupport<Task, TaskDto> {

    public TaskAssembler() {
        super(TaskApiController.class, TaskDto.class);
    }

    @Override
    public TaskDto toModel(Task task) {
        TaskDto taskDto = new TaskDto(task.getTitle(), task.getDescription(), task.getPriority());

        WebMvcLinkBuilder link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                .methodOn(TaskApiController.class)
                .getOne(task.getTaskId()));

        taskDto.add(link.withSelfRel());
        return taskDto;
    }
}