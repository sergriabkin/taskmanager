package com.taskmanager.model;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Entity
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @Column
    private String title;

    @Column
    private String description;

    @Range(min = 0, max = 5)
    @Column
    private Integer priority = 3;

    public Task() {
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Task(String title, String description, @Range(min = 0, max = 5) Integer priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }
}
