package com.example.blogging.dto;

public record AuthResponse(
        String message,
        UserResponse user
) {
}
