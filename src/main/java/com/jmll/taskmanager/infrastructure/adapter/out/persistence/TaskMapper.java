package com.jmll.taskmanager.infrastructure.adapter.out.persistence;

import com.jmll.taskmanager.application.domain.task.model.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class TaskMapper {
    public abstract TaskJpaEntity taskToTaskJpaEntity(Task task);

    public abstract Task taskJpaEntityToTask(TaskJpaEntity taskJpaEntity);
}