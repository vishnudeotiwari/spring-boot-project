package com.example.blogging.dto;

import com.example.blogging.domain.PostStatus;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
        Long id,
        String title,
        String slug,
        String excerpt,
        String content,
        PostStatus status,
        LocalDateTime publishedAt,
        UserResponse author,
        CategoryResponse category,
        List<TagResponse> tags,
        long commentCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
