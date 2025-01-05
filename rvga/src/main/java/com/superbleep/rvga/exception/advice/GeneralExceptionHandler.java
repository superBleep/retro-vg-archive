package com.superbleep.rvga.exception.advice;

import com.superbleep.rvga.exception.*;
import com.superbleep.rvga.exception.general.BadRequestException;
import com.superbleep.rvga.exception.general.ForbiddenException;
import com.superbleep.rvga.exception.general.NotFoundException;
import com.superbleep.rvga.util.InvalidFieldsResponse;
import com.superbleep.rvga.util.InvalidValuesResponse;
import com.superbleep.rvga.util.MessageResponse;
import com.superbleep.rvga.util.SqlResponse;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GeneralExceptionHandler {
    @ExceptionHandler({ArchiveUserNotFound.class, PlatformNotFound.class, GameNotFound.class, GameVersionNotFound.class})
    public ResponseEntity<Object> handle(NotFoundException e) {
        MessageResponse res = new MessageResponse(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(res);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, GameIdenticalFound.class})
    public ResponseEntity<Object> handle(DataAccessException e) {
        SqlResponse res = new SqlResponse("Deteceted conflict in the database",
                e.getMostSpecificCause().getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(res);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handle(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        BindingResult bRes = e.getBindingResult();
        List<FieldError> invalidFields = bRes.getFieldErrors();

        for (FieldError invalidField : invalidFields)
            errors.put(invalidField.getField(), invalidField.getDefaultMessage());

        InvalidFieldsResponse res = new InvalidFieldsResponse(errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(res);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handle(TypeMismatchException e) {
        MessageResponse res = new MessageResponse(STR."Value \{e.getValue()} of parameter \{e.getPropertyName()} is invalid.");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(res);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<Object> hanlde(HttpMessageNotReadableException e) {
        InvalidValuesResponse res = new InvalidValuesResponse("Malformed JSON body", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(res);
    }

    @ExceptionHandler({ArchiveUserPasswordsIdentical.class,
            ArchiveUserEmptyBody.class, ArchiveUserRolesIdentical.class, ArchiveUserRoleNotFound.class,
            PlatformEmptyBody.class, GameVersionEmptyBody.class, GameVersionOnlyOne.class})
    public ResponseEntity<Object> handle(BadRequestException e) {
        MessageResponse res = new MessageResponse(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(res);
    }

    @ExceptionHandler({ArchiveUserRolesForbidden.class})
    public ResponseEntity<Object> hanlde(ForbiddenException e) {
        MessageResponse res = new MessageResponse(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(res);
    }
}
