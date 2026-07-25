package com.example.adapter.in.rest.dto;

import com.example.adapter.in.rest.validation.ValidUrl;

public record CreateUrlRequestDto(
        @ValidUrl
        String url) {
}
