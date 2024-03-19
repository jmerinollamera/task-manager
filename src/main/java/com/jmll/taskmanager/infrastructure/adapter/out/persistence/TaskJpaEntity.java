package com.jmll.taskmanager.infrastructure.adapter.out.persistence;

import com.jmll.taskmanager.application.domain.task.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "task")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskJpaEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String description;
    private LocalDate expiryDate;
    private TaskStatus status;
    @OneToMany
    private List<TaskJpaEntity> tasks;
}