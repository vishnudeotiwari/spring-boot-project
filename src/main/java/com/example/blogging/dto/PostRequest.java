package com.example.blogging.dto;

import com.example.blogging.domain.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record PostRequest(
        @NotBlank
        @Size(max = 180)
        String title,

        @Size(max = 220)
        String slug,

        @Size(max = 400)
        String excerpt,

        @NotBlank
        String content,

        PostStatus status,

        @NotNull
        Long authorId,

        Long categoryId,

        Set<Long> tagIds
) {
}
