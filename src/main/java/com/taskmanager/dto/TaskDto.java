package com.taskmanager.dto;

import com.taskmanager.model.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class TaskDto extends RepresentationModel<TaskDto> {
    @Size(max = 100)
    private String title;
    @Size(max = 100)
    private String description;
    @Min(1)
    @Max(5)
    private Integer priority = 3;

    public TaskDto(@Size(max = 100) String title, @Size(max = 100) String description) {
        this.title = title;
        this.description = description;
    }

    public Task toTask(){
        return new Task(title, description, priority);
    }
}