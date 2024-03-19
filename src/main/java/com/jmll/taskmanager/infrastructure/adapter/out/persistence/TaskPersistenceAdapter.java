package com.jmll.taskmanager.infrastructure.adapter.out.persistence;

import com.jmll.taskmanager.application.domain.task.model.Task;
import com.jmll.taskmanager.application.port.out.TaskPersistencePort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service("taskPersistencePort")
public class TaskPersistenceAdapter implements TaskPersistencePort {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public Task getTask(Long taskId) {
        if (taskRepository.existsById(taskId)) {
            TaskJpaEntity taskJpaEntity = taskRepository.getReferenceById(taskId);
            return taskMapper.taskJpaEntityToTask(taskJpaEntity);
        }
        return null;
    }

    @Override
    public Task saveTask(Task task) {
        TaskJpaEntity taskJpaEntity = taskMapper.taskToTaskJpaEntity(task);
        taskJpaEntity = taskRepository.save(taskJpaEntity);
        return taskMapper.taskJpaEntityToTask(taskJpaEntity);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::taskJpaEntityToTask).toList();
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}