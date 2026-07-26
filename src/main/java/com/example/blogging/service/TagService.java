package com.example.blogging.service;

import com.example.blogging.domain.Tag;
import com.example.blogging.dto.TagRequest;
import com.example.blogging.dto.TagResponse;
import com.example.blogging.exception.BadRequestException;
import com.example.blogging.exception.ResourceNotFoundException;
import com.example.blogging.mapper.BlogMapper;
import com.example.blogging.repository.TagRepository;
import com.example.blogging.util.SlugUtil;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagResponse create(TagRequest request) {
        String slug = SlugUtil.toSlug(request.name());
        if (tagRepository.existsBySlug(slug)) {
            throw new BadRequestException("Tag already exists: " + request.name());
        }

        Tag tag = new Tag(request.name().trim(), slug);
        return BlogMapper.toTagResponse(tagRepository.save(tag));
    }

    @Transactional(readOnly = true)
    public List<TagResponse> findAll() {
        return tagRepository.findAll(Sort.by("name")).stream()
                .map(BlogMapper::toTagResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TagResponse findById(Long id) {
        return BlogMapper.toTagResponse(requireTag(id));
    }

    public TagResponse update(Long id, TagRequest request) {
        Tag tag = requireTag(id);
        String slug = SlugUtil.toSlug(request.name());
        tagRepository.findBySlug(slug)
                .filter(existing -> !Objects.equals(existing.getId(), id))
                .ifPresent(existing -> {
                    throw new BadRequestException("Tag already exists: " + request.name());
                });

        tag.setName(request.name().trim());
        tag.setSlug(slug);
        return BlogMapper.toTagResponse(tag);
    }

    public void delete(Long id) {
        Tag tag = requireTag(id);
        tagRepository.delete(tag);
    }

    @Transactional(readOnly = true)
    public Tag requireTag(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id " + id));
    }
}
