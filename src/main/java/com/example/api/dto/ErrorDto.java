package com.example.api.dto;

import java.util.List;

public record ErrorDto(String message, List<FieldErrorDto> errors) {
}
