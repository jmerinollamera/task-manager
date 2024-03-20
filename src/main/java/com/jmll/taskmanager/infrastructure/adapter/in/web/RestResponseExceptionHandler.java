package com.jmll.taskmanager.infrastructure.adapter.in.web;

import com.jmll.taskmanager.application.domain.task.service.exception.DuplicateTaskException;
import com.jmll.taskmanager.application.domain.task.service.exception.TaskNotFoundException;
import com.jmll.taskmanager.application.domain.task.service.exception.UserCantDeleteTaskException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestResponseExceptionHandler {
    @ExceptionHandler(value
            = {DuplicateTaskException.class})
    protected ResponseEntity<Object> handleDuplicateTaskException(
            RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value
            = {TaskNotFoundException.class})
    protected ResponseEntity<Object> handleTaskNotFoundException(
            RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value
            = {UserCantDeleteTaskException.class})
    protected ResponseEntity<Object> handleUserCantDeleteException(
            RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }
}