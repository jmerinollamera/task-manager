package com.jmll.taskmanager.domain.task.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class Task {
    private Long id;
    @NonNull
    private String description;
    @NonNull
    private LocalDate expiryDate;
    private TaskStatus status;
    private List<Task> subtasks;
    private Task parentTask;
}