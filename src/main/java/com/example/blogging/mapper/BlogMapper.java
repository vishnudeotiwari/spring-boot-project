package com.example.blogging.mapper;

import com.example.blogging.domain.Category;
import com.example.blogging.domain.Comment;
import com.example.blogging.domain.Post;
import com.example.blogging.domain.Tag;
import com.example.blogging.domain.User;
import com.example.blogging.dto.CategoryResponse;
import com.example.blogging.dto.CommentResponse;
import com.example.blogging.dto.PostResponse;
import com.example.blogging.dto.TagResponse;
import com.example.blogging.dto.UserResponse;

import java.util.Comparator;

public final class BlogMapper {

    private BlogMapper() {
    }

    public static UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getBio(), user.getCreatedAt());
    }

    public static CategoryResponse toCategoryResponse(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryResponse(category.getId(), category.getName(), category.getSlug(), category.getDescription());
    }

    public static TagResponse toTagResponse(Tag tag) {
        return new TagResponse(tag.getId(), tag.getName(), tag.getSlug());
    }

    public static CommentResponse toCommentResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getAuthorName(),
                comment.getAuthorEmail(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }

    public static PostResponse toPostResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getSlug(),
                post.getExcerpt(),
                post.getContent(),
                post.getStatus(),
                post.getPublishedAt(),
                toUserResponse(post.getAuthor()),
                toCategoryResponse(post.getCategory()),
                post.getTags().stream()
                        .sorted(Comparator.comparing(Tag::getName))
                        .map(BlogMapper::toTagResponse)
                        .toList(),
                post.getComments().size(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
