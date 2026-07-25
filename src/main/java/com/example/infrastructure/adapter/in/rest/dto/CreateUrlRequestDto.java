package com.example.infrastructure.adapter.in.rest.dto;

import com.example.infrastructure.adapter.in.rest.validation.ValidUrl;

public record CreateUrlRequestDto(
        @ValidUrl
        String url) {
}
