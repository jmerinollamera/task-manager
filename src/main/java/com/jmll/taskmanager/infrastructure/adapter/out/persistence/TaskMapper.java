package com.jmll.taskmanager.infrastructure.adapter.out.persistence;

import com.jmll.taskmanager.domain.task.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class TaskMapper {

    @Mapping(target = "parentTask.subtasks", ignore = true)
    public abstract TaskJpaEntity taskToTaskJpaEntity(Task task);

    @Mapping(target = "parentTask.subtasks", ignore = true)
    public abstract Task taskJpaEntityToTask(TaskJpaEntity taskJpaEntity);

}