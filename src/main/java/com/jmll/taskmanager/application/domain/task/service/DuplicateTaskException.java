package com.jmll.taskmanager.application.domain.task.service;

public class DuplicateTaskException extends RuntimeException {
    public DuplicateTaskException(Long taskId) {
        super(String.format("The Tasks with id %s already exists", taskId));
    }
}