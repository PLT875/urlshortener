package com.example.adapter.in.rest.dto;

import java.util.List;

public record ErrorDto(String message, List<FieldErrorDto> errors) {
}
