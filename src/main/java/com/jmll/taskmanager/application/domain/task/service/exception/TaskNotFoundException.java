package com.jmll.taskmanager.application.domain.task.service.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long taskId) {
        super(String.format("The Task with id %s doesn't exists", taskId));
    }
}