package com.example.presentation.controller.dto;

import java.util.List;

public record ErrorDto(String message, List<FieldErrorDto> errors) {
}
