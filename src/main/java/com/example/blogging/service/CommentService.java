package com.example.blogging.service;

import com.example.blogging.domain.Comment;
import com.example.blogging.domain.Post;
import com.example.blogging.dto.CommentRequest;
import com.example.blogging.dto.CommentResponse;
import com.example.blogging.dto.PageResponse;
import com.example.blogging.exception.ResourceNotFoundException;
import com.example.blogging.mapper.BlogMapper;
import com.example.blogging.repository.CommentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;

    public CommentService(CommentRepository commentRepository, PostService postService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
    }

    public CommentResponse create(Long postId, CommentRequest request) {
        Post post = postService.requirePost(postId);
        Comment comment = new Comment(
                request.authorName().trim(),
                request.authorEmail().trim().toLowerCase(),
                request.content().trim(),
                post
        );
        return BlogMapper.toCommentResponse(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    public PageResponse<CommentResponse> findByPost(Long postId, int page, int size) {
        postService.requirePost(postId);
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);
        var pageable = PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        return PageResponse.from(commentRepository.findByPostId(postId, pageable).map(BlogMapper::toCommentResponse));
    }

    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + id));
        commentRepository.delete(comment);
    }
}
