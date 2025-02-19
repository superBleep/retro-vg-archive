package com.superbleep.nrvga.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<Object> handle(HttpMessageNotReadableException e) {
        Map<String, String> res = new HashMap<>();

        if(e.getCause() instanceof InvalidFormatException inve) {
            Class<?> type = inve.getTargetType();

            if(type.isEnum()) {
                Object value = inve.getValue();
                String field = inve.getPath().stream()
                        .map(ref -> ref.getFieldName() != null ? ref.getFieldName() : "unknown")
                        .findFirst()
                        .orElse("unknown");

                res.put(field, messageSource.getMessage(
                        "archive.user.role.bad.value", new Object[] {value}, Locale.getDefault()));

                return ResponseEntity
                        .badRequest()
                        .body(res);
            }
        }

        return ResponseEntity
                .badRequest()
                .header("Content-Type", "text/plain")
                .body("Malformed JSON body");
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handle(MethodArgumentNotValidException e) {
        String[] messages = Arrays
                .stream(e.getDetailMessageArguments())
                .map(Object::toString)
                .toArray(String[]::new);

        List<String> stringFields = List.of(messages[1].split(", and "));

        Map<String, String> objectFields = new HashMap<>();

        for(String field : stringFields) {
            List<String> pair = List.of(field.split(": "));

            objectFields.put(pair.getFirst(), pair.getLast());
        }

        return ResponseEntity
                .badRequest()
                .body(objectFields);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handle(RuntimeException exception) {
        return ResponseEntity
                .badRequest()
                .header("Content-Type", "text/plain")
                .body(exception.getMessage());
    }
}
