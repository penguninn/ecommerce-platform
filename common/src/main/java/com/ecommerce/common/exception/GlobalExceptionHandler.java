package com.ecommerce.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getAllErrors().forEach(error -> errors.put(error.getCode(), error.getDefaultMessage()));
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setType(URI.create("https://www.example.com/validation-error"));
        pd.setTitle("Validation Error");
        pd.setDetail("One or more fields have an error.");
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("errors", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(pd);
    }

}
