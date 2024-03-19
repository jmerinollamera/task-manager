package com.jmll.taskmanager.infrastructure.adapter.in.web;

import com.jmll.taskmanager.application.domain.task.model.Task;
import com.jmll.taskmanager.application.domain.task.service.TaskManagerService;
import com.jmll.taskmanager.application.domain.task.service.UserCantDeleteTaskException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("task")
public class TaskController {

    private final TaskManagerService taskManagerService;

    @PostMapping("")
    public Task addTask(Task task) {
        return taskManagerService.addTask(task);
    }

    @GetMapping("/all")
    public List<Task> getAllTasks() {
        return taskManagerService.getAllTasks();
    }

    @PostMapping("/implemented/{taskId}")
    public Task setTaskAsImplemented(@PathVariable("taskId") Long taskId) {
        return taskManagerService.setTaskAsImplemented(taskId);
    }

    @DeleteMapping("")
    public Boolean deleteTask(Long taskId) throws UserCantDeleteTaskException {
        taskManagerService.deleteTask(taskId);
        return true;
    }

}