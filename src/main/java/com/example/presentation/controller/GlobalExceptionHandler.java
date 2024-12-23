package com.example.presentation.controller;

import com.example.presentation.controller.dto.ErrorDto;
import com.example.presentation.controller.dto.FieldErrorDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorDto(e.getBindingResult()));
    }

    private ErrorDto errorDto(BindingResult bindingResult) {
        FieldError[] fieldErrors = bindingResult.getFieldErrors().toArray(new FieldError[]{});
        List<FieldErrorDto> fieldErrorDtos = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            fieldErrorDtos.add(new FieldErrorDto(
                    fieldError.getDefaultMessage(),
                    fieldError.getField(),
                    (String) fieldError.getRejectedValue()));
        }

        return new ErrorDto("request body contains errors", fieldErrorDtos);
    }
}
