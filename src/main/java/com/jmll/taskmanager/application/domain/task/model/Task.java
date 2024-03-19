package com.jmll.taskmanager.application.domain.task.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
 
@Data
public class Task {
    private Long id;
    private String description;
    private LocalDate expiryDate;
    private TaskStatus status;
    private List<Task> tasks;
    private Task parentTask;
}