package com.example.presentation.controller.dto;

import lombok.NonNull;

public record CreateUrlRequestDto(
        // todo: validate URL pattern
        @NonNull
        String url) {
}
