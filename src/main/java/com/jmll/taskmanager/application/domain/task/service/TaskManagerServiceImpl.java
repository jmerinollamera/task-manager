package com.jmll.taskmanager.application.domain.task.service;

import com.jmll.taskmanager.application.domain.task.model.Task;
import com.jmll.taskmanager.application.domain.task.model.TaskStatus;
import com.jmll.taskmanager.application.domain.task.service.exception.DuplicateTaskException;
import com.jmll.taskmanager.application.domain.task.service.exception.TaskNotFoundException;
import com.jmll.taskmanager.application.domain.task.service.exception.UserCantDeleteTaskException;
import com.jmll.taskmanager.application.port.out.TaskPersistencePort;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskManagerServiceImpl implements TaskManagerService {

    private static final TaskStatus TASK_STATUS_BY_DEFAULT = TaskStatus.PENDING;
    private final TaskPersistencePort taskPersistencePort;

    @Override
    public Task addTask(Task task) throws DuplicateTaskException {
        if (task.getId() != null && taskPersistencePort.getTask(task.getId()) != null) {
            throw new DuplicateTaskException(task.getId());
        }
        task.setStatus(TASK_STATUS_BY_DEFAULT);
        if (CollectionUtils.isNotEmpty(task.getSubtasks())) {
            this.setSubTasksStatus(task, TASK_STATUS_BY_DEFAULT);
        }
        return taskPersistencePort.saveTask(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskPersistencePort.getAllTasks();
    }

    @Override
    public Task setTaskAsImplemented(Long taskId) {
        return changeTaskStatus(taskId, TaskStatus.IMPLEMENTED);
    }

    @Override
    public Task setTaskAsPending(Long taskId) {
        return changeTaskStatus(taskId, TaskStatus.PENDING);
    }

    @Override
    public void deleteTask(Long taskId) throws UserCantDeleteTaskException {
        Task task = validateOrRetrieveTask(taskId);
        if (CollectionUtils.isNotEmpty(task.getSubtasks())) {
            task.getSubtasks().forEach(sonTask -> taskPersistencePort.deleteTask(sonTask.getId()));
        }
        taskPersistencePort.deleteTask(taskId);
    }

    private Task validateOrRetrieveTask(Long taskId) {
        if (taskPersistencePort.getTask(taskId) == null) {
            throw new TaskNotFoundException(taskId);
        } else {
            return taskPersistencePort.getTask(taskId);
        }
    }

    private Task changeTaskStatus(Long taskId, TaskStatus taskStatus) {
        Task task = validateOrRetrieveTask(taskId);
        task.setStatus(taskStatus);
        if (CollectionUtils.isNotEmpty(task.getSubtasks())) {
            return processTaskWithSubtask(task, taskStatus);
        } else if (task.getParentTask() != null) {
            return processTaskWithParent(task, taskStatus);
        } else {
            return taskPersistencePort.saveTask(task);
        }
    }

    private Task processTaskWithSubtask(Task task, TaskStatus taskStatus) {
        this.setSubTasksStatus(task, taskStatus);
        return taskPersistencePort.saveTask(task);
    }

    private Task processTaskWithParent(Task task, TaskStatus taskStatus) {
        task = taskPersistencePort.saveTask(task);
        Task parentTask = taskPersistencePort.getTask(task.getParentTask().getId());
        Boolean existsTask = parentTask.getSubtasks().stream()
                .anyMatch(sonTask -> !sonTask.getStatus().equals(taskStatus));
        if (TaskStatus.IMPLEMENTED.equals(taskStatus) && Boolean.FALSE.equals(existsTask)) {
            parentTask.setStatus(taskStatus);
            taskPersistencePort.saveTask(parentTask);
        } else {
            parentTask.setStatus(TASK_STATUS_BY_DEFAULT);
            taskPersistencePort.saveTask(parentTask);
        }
        return task;
    }

    private void setSubTasksStatus(Task task, TaskStatus taskStatus) {
        task.getSubtasks().forEach(innerTask -> {
            innerTask.setStatus(taskStatus);
            innerTask.setParentTask(task);
        });
    }

}