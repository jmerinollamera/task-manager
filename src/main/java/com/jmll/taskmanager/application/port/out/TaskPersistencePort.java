package com.jmll.taskmanager.application.port.out;

import com.jmll.taskmanager.application.domain.task.model.Task;

import java.util.List;

public interface TaskPersistencePort {

    Task getTask(Long taskId);

    Task saveTask(Task task);

    List<Task> getAllTasks();

    void deleteTask(Long taskId);
}