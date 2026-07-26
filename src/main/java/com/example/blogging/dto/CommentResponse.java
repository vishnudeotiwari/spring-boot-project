package com.example.blogging.dto;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String authorName,
        String authorEmail,
        String content,
        LocalDateTime createdAt
) {
}
