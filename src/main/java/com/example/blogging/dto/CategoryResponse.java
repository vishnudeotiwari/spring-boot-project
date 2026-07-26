package com.example.blogging.dto;

public record CategoryResponse(
        Long id,
        String name,
        String slug,
        String description
) {
}
