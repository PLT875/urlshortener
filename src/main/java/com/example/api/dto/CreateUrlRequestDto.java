package com.example.api.dto;

import com.example.api.validation.ValidUrl;

public record CreateUrlRequestDto(
        @ValidUrl
        String url) {
}
