package com.quest.etna.exception.handler;

import com.quest.etna.exception.UnauthorizedAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;

@ControllerAdvice
public class UnauthorizedAccessExceptionHandler {

    @ExceptionHandler(UnauthorizedAccessException.class)
    protected ResponseEntity<?> unauthorized() {
        LinkedHashMap<String, Object> responseHashMap = new LinkedHashMap<>();
        responseHashMap.put("message", "Unauthorized");

        return new ResponseEntity<>(responseHashMap, HttpStatus.UNAUTHORIZED);
    }
}