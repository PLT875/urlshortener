package com.example.presentation.controller.dto;

import com.example.presentation.controller.validation.ValidUrl;

public record CreateUrlRequestDto(
        @ValidUrl
        String url) {
}
