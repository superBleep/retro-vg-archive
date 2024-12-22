package com.superbleep.rvga.exception.advice;

import com.superbleep.rvga.exception.ArchiveUserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GeneralExceptionHandler {
    @ExceptionHandler({ArchiveUserNotFoundException.class})
    public ResponseEntity<String> handle(ArchiveUserNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(STR."\{e.getMessage()} at \{LocalDateTime.now()}");
    }
}
