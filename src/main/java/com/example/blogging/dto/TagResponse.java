package com.example.blogging.dto;

public record TagResponse(
        Long id,
        String name,
        String slug
) {
}
