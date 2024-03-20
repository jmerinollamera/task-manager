package com.jmll.taskmanager.application.domain.task.service;

import com.jmll.taskmanager.application.domain.task.model.Task;
import com.jmll.taskmanager.application.domain.task.service.exception.DuplicateTaskException;
import com.jmll.taskmanager.application.domain.task.service.exception.UserCantDeleteTaskException;

import java.util.List;

public interface TaskManagerService {

    Task addTask(Task task) throws DuplicateTaskException;

    List<Task> getAllTasks();

    Task setTaskAsImplemented(Long taskId);

    Task setTaskAsPending(Long taskId);

    void deleteTask(Long taskId) throws UserCantDeleteTaskException;

}