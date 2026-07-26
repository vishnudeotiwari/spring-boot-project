package com.example.blogging.dto;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String name,
        String email,
        String bio,
        LocalDateTime createdAt
) {
}
