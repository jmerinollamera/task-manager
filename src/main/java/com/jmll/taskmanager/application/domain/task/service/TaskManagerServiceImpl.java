package com.jmll.taskmanager.application.domain.task.service;

import com.jmll.taskmanager.application.domain.task.model.Task;
import com.jmll.taskmanager.application.domain.task.model.TaskStatus;
import com.jmll.taskmanager.application.port.out.TaskPersistencePort;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskManagerServiceImpl implements TaskManagerService {

    private static final Boolean IS_VALID_USER = Boolean.TRUE;
    private final TaskPersistencePort taskPersistencePort;

    @Override
    public Task addTask(Task task) throws DuplicateTaskException {
        if (task.getId() != null && taskPersistencePort.getTask(task.getId()) != null) {
            throw new DuplicateTaskException(task.getId());
        }
        task.setStatus(TaskStatus.PENDING);
        return taskPersistencePort.saveTask(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskPersistencePort.getAllTasks();
    }

    @Override
    public Task setTaskAsImplemented(Long taskId) {
        Task task = taskPersistencePort.getTask(taskId);
        task.setStatus(TaskStatus.IMPLEMENTED);
        if (CollectionUtils.isNotEmpty(task.getTasks())) {
            return processTasks(task);
        }
        if (task.getParentTask() != null) {
            return processParentStatusTask(task);
        }
        return taskPersistencePort.saveTask(task);
    }

    private Task processTasks(Task task) {
        task.getTasks().forEach(innerTask -> innerTask.setStatus(TaskStatus.IMPLEMENTED));
        return taskPersistencePort.saveTask(task);
    }

    private Task processParentStatusTask(Task task) {
        Task parentTask = taskPersistencePort.getTask(task.getParentTask().getId());
        Task existsTask = parentTask.getTasks().stream().filter(sonTask -> sonTask.getStatus().equals(TaskStatus.PENDING)).findAny().orElse(null);
        if (existsTask == null) {
            parentTask.setStatus(TaskStatus.IMPLEMENTED);
            taskPersistencePort.saveTask(parentTask);
        }
        return taskPersistencePort.saveTask(task);
    }

    @Override
    public void deleteTask(Long taskId) throws UserCantDeleteTaskException {
        Task task = taskPersistencePort.getTask(taskId);
        if (CollectionUtils.isNotEmpty(task.getTasks())) {
            task.getTasks().forEach(sonTask -> taskPersistencePort.deleteTask(sonTask.getId()));
        }
        taskPersistencePort.deleteTask(taskId);
    }

}