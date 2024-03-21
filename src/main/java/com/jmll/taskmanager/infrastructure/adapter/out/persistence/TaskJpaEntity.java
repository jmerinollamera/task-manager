package com.jmll.taskmanager.infrastructure.adapter.out.persistence;

import com.jmll.taskmanager.domain.task.model.TaskStatus;
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
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private LocalDate expiryDate;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    @OneToMany(mappedBy = "parentTask", cascade = CascadeType.ALL)
    private List<TaskJpaEntity> subtasks;
    @ManyToOne
    @JoinColumn(name = "parent_task_id")
    private TaskJpaEntity parentTask;
}