package com.example.domain.model;

import jakarta.validation.constraints.NotNull;

public record Url(@NotNull String id, @NotNull String longUrl) {
}
