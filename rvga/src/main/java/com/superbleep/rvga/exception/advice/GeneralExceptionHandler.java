package com.superbleep.rvga.exception.advice;

import com.superbleep.rvga.exception.*;
import com.superbleep.rvga.exception.general.BadRequestException;
import com.superbleep.rvga.exception.general.ForbiddenException;
import com.superbleep.rvga.exception.general.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GeneralExceptionHandler {
    @ExceptionHandler({ArchiveUserNotFound.class})
    public ResponseEntity<Object> handle(NotFoundException e) {
        Map<String, String> obj = new HashMap<>();
        obj.put("message", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(obj);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handle(DataIntegrityViolationException e) {
        Map<String, String> obj = new HashMap<>();
        obj.put("message", "There's already an identical entity in the database");
        obj.put("sqlMessage", e.getMostSpecificCause().getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(obj);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handle(MethodArgumentNotValidException e) {
        Map<String, Object> obj = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        BindingResult res = e.getBindingResult();
        List<FieldError> invalidFields = res.getFieldErrors();

        for (FieldError invalidField : invalidFields) {
            errors.put(invalidField.getField(), invalidField.getDefaultMessage());
        }

        obj.put("invalidFields", errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(obj);
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            ArchiveUserPasswordsIdentical.class,
            ArchiveUserEmptyBody.class,
            ArchiveUserRolesIdentical.class,
            ArchiveUserRoleNotFound.class
    })
    public ResponseEntity<Object> handle(BadRequestException e) {
        Map<String, String> obj = new HashMap<>();

        obj.put("message", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(obj);
    }

    @ExceptionHandler({ArchiveUserRolesForbidden.class})
    public ResponseEntity<Object> hanlde(ForbiddenException e) {
        Map<String, String> obj = new HashMap<>();

        obj.put("message", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(obj);
    }
}
