package com.jmll.taskmanager.application.domain.task.service;

import com.jmll.taskmanager.application.domain.task.model.Task;
import com.jmll.taskmanager.application.domain.task.model.TaskStatus;
import com.jmll.taskmanager.application.domain.task.service.exception.DuplicateTaskException;
import com.jmll.taskmanager.application.domain.task.service.exception.UserCantDeleteTaskException;
import com.jmll.taskmanager.application.port.out.TaskPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class TaskManagerServiceImplTest {

    public static final long TASK_DEFAULT_ID = 1L;
    @Mock
    private TaskPersistencePort taskPersistencePort;

    private TaskManagerServiceImpl taskManagerService;

    @BeforeEach
    void setUp() {
        taskManagerService = new TaskManagerServiceImpl(taskPersistencePort);
    }

    @Test
    void addTask_NewTask_ShouldReturnTaskWithPendingStatus() {
        Task taskToAdd = new Task();
        taskToAdd.setId(TASK_DEFAULT_ID);

        when(taskPersistencePort.getTask(TASK_DEFAULT_ID)).thenReturn(null);
        when(taskPersistencePort.saveTask(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task resultTask = taskManagerService.addTask(taskToAdd);

        assertNotNull(resultTask);
        assertEquals(TaskStatus.PENDING, resultTask.getStatus());
    }

    @Test
    void addTask_ExistingTask_ShouldThrowDuplicateTaskException() {
        Task existingTask = new Task();
        existingTask.setId(TASK_DEFAULT_ID);

        when(taskPersistencePort.getTask(TASK_DEFAULT_ID)).thenReturn(existingTask);

        assertThrows(DuplicateTaskException.class, () -> taskManagerService.addTask(existingTask));
    }

    @Test
    void getAllTasks_ShouldReturnListOfTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task());
        tasks.add(new Task());

        when(taskPersistencePort.getAllTasks()).thenReturn(tasks);

        List<Task> resultTasks = taskManagerService.getAllTasks();

        assertNotNull(resultTasks);
        assertEquals(tasks.size(), resultTasks.size());
    }

    @Test
    void setTaskAsImplemented_WithoutSubtasks_ShouldUpdateTaskStatus() {
        Task taskToUpdate = new Task();
        taskToUpdate.setId(TASK_DEFAULT_ID);
        taskToUpdate.setStatus(TaskStatus.PENDING);

        when(taskPersistencePort.getTask(TASK_DEFAULT_ID)).thenReturn(taskToUpdate);
        when(taskPersistencePort.saveTask(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task resultTask = taskManagerService.setTaskAsImplemented(TASK_DEFAULT_ID);

        assertNotNull(resultTask);
        assertEquals(TaskStatus.IMPLEMENTED, resultTask.getStatus());
    }

    @Test
    void setTaskAsImplemented_WithSubtasks_ShouldUpdateSubtasksStatusAndParentTaskStatus() {
        Task parentTask = new Task();
        parentTask.setId(TASK_DEFAULT_ID);
        Task subtask1 = new Task();
        subtask1.setStatus(TaskStatus.PENDING);
        Task subtask2 = new Task();
        subtask2.setStatus(TaskStatus.PENDING);
        List<Task> tasks = new ArrayList<>();
        tasks.add(subtask1);
        tasks.add(subtask2);
        parentTask.setSubtasks(tasks);

        when(taskPersistencePort.getTask(TASK_DEFAULT_ID)).thenReturn(parentTask);
        when(taskPersistencePort.saveTask(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task resultTask = taskManagerService.setTaskAsImplemented(TASK_DEFAULT_ID);

        assertNotNull(resultTask);
        assertEquals(TaskStatus.IMPLEMENTED, resultTask.getStatus());
        assertTrue(parentTask.getSubtasks().stream().allMatch(t -> t.getStatus() == TaskStatus.IMPLEMENTED));
    }

    @Test
    void deleteTask_ValidUser_ShouldDeleteTask() throws UserCantDeleteTaskException {
        long taskIdToDelete = TASK_DEFAULT_ID;

        when(taskPersistencePort.getTask(taskIdToDelete)).thenReturn(new Task());

        assertDoesNotThrow(() -> taskManagerService.deleteTask(taskIdToDelete));
    }

}