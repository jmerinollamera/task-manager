package com.jmll.taskmanager.application.domain.task.service;

public class UserCantDeleteTaskException extends RuntimeException {
    public UserCantDeleteTaskException(Long taskId) {
        super(String.format("The User can Delete Task with id %s", taskId));
    }
}