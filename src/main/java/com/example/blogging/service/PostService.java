package com.example.blogging.service;

import com.example.blogging.domain.Category;
import com.example.blogging.domain.Post;
import com.example.blogging.domain.PostStatus;
import com.example.blogging.domain.Tag;
import com.example.blogging.domain.User;
import com.example.blogging.dto.PageResponse;
import com.example.blogging.dto.PostRequest;
import com.example.blogging.dto.PostResponse;
import com.example.blogging.exception.BadRequestException;
import com.example.blogging.exception.ResourceNotFoundException;
import com.example.blogging.mapper.BlogMapper;
import com.example.blogging.repository.PostRepository;
import com.example.blogging.repository.TagRepository;
import com.example.blogging.util.SlugUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    public PostService(
            PostRepository postRepository,
            TagRepository tagRepository,
            UserService userService,
            CategoryService categoryService
    ) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    public PostResponse create(PostRequest request) {
        User author = userService.requireUser(request.authorId());
        Category category = request.categoryId() == null ? null : categoryService.requireCategory(request.categoryId());
        String baseSlug = SlugUtil.toSlug(request.slug() == null || request.slug().isBlank() ? request.title() : request.slug());

        Post post = new Post(
                request.title().trim(),
                uniqueSlug(baseSlug, null),
                cleanOptional(request.excerpt()),
                request.content().trim(),
                request.status() == null ? PostStatus.DRAFT : request.status(),
                author,
                category
        );
        post.setTags(resolveTags(request.tagIds()));
        return BlogMapper.toPostResponse(postRepository.save(post));
    }

    @Transactional(readOnly = true)
    public PageResponse<PostResponse> search(
            String query,
            PostStatus status,
            Long authorId,
            Long categoryId,
            Long tagId,
            int page,
            int size
    ) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);
        var pageable = PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        var result = postRepository.search(cleanOptional(query), status, authorId, categoryId, tagId, pageable)
                .map(BlogMapper::toPostResponse);
        return PageResponse.from(result);
    }

    @Transactional(readOnly = true)
    public PostResponse findById(Long id) {
        return BlogMapper.toPostResponse(requirePost(id));
    }

    @Transactional(readOnly = true)
    public PostResponse findBySlug(String slug) {
        return BlogMapper.toPostResponse(postRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with slug " + slug)));
    }

    public PostResponse update(Long id, PostRequest request) {
        Post post = requirePost(id);
        User author = userService.requireUser(request.authorId());
        Category category = request.categoryId() == null ? null : categoryService.requireCategory(request.categoryId());

        post.setTitle(request.title().trim());
        if (request.slug() != null && !request.slug().isBlank()) {
            post.setSlug(uniqueSlug(SlugUtil.toSlug(request.slug()), post.getId()));
        }
        post.setExcerpt(cleanOptional(request.excerpt()));
        post.setContent(request.content().trim());
        post.setStatus(request.status() == null ? PostStatus.DRAFT : request.status());
        post.setAuthor(author);
        post.setCategory(category);
        post.setTags(resolveTags(request.tagIds()));
        return BlogMapper.toPostResponse(post);
    }

    public PostResponse publish(Long id) {
        Post post = requirePost(id);
        post.setStatus(PostStatus.PUBLISHED);
        return BlogMapper.toPostResponse(post);
    }

    public PostResponse archive(Long id) {
        Post post = requirePost(id);
        post.setStatus(PostStatus.ARCHIVED);
        return BlogMapper.toPostResponse(post);
    }

    public void delete(Long id) {
        Post post = requirePost(id);
        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public Post requirePost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + id));
    }

    private Set<Tag> resolveTags(Set<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return new LinkedHashSet<>();
        }

        Set<Tag> tags = new LinkedHashSet<>(tagRepository.findByIdIn(tagIds));
        if (tags.size() != tagIds.size()) {
            throw new ResourceNotFoundException("One or more tags were not found");
        }
        return tags;
    }

    private String uniqueSlug(String baseSlug, Long existingPostId) {
        String candidate = baseSlug;
        int suffix = 2;
        while (postRepository.findBySlug(candidate)
                .filter(existing -> !Objects.equals(existing.getId(), existingPostId))
                .isPresent()) {
            candidate = baseSlug + "-" + suffix;
            suffix++;
        }
        return candidate;
    }

    private String cleanOptional(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
