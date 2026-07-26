package com.example.blogging.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotBlank
        @Size(max = 120)
        String authorName,

        @NotBlank
        @Email
        @Size(max = 180)
        String authorEmail,

        @NotBlank
        @Size(max = 2000)
        String content
) {
}
