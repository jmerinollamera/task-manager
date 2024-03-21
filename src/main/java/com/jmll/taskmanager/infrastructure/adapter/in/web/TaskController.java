package com.jmll.taskmanager.infrastructure.adapter.in.web;

import com.jmll.taskmanager.domain.task.model.Task;
import com.jmll.taskmanager.domain.task.service.TaskManagerService;
import com.jmll.taskmanager.domain.task.service.exception.UserCantDeleteTaskException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("tasks")
public class TaskController {

    private final TaskManagerService taskManagerService;

    @PostMapping(value = "", produces = "application/json")
    public Task addTask(@RequestBody Task task) {
        return taskManagerService.addTask(task);
    }

    @GetMapping(value = "/all", produces = "application/json")
    public List<Task> getAllTasks() {
        return taskManagerService.getAllTasks();
    }

    @PostMapping(value = "/implemented/{taskId}", produces = "application/json")
    public Task setTaskAsImplemented(@PathVariable("taskId") Long taskId) {
        return taskManagerService.setTaskAsImplemented(taskId);
    }

    @PostMapping(value = "/pending/{taskId}", produces = "application/json")
    public Task setTaskAsPending(@PathVariable("taskId") Long taskId) {
        return taskManagerService.setTaskAsPending(taskId);
    }

    @DeleteMapping(value = "/{taskId}")
    public void deleteTask(@PathVariable("taskId") Long taskId) throws UserCantDeleteTaskException {
        taskManagerService.deleteTask(taskId);
    }

}