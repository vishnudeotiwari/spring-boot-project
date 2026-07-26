package com.example.blogging.service;

import com.example.blogging.domain.Category;
import com.example.blogging.dto.CategoryRequest;
import com.example.blogging.dto.CategoryResponse;
import com.example.blogging.exception.BadRequestException;
import com.example.blogging.exception.ResourceNotFoundException;
import com.example.blogging.mapper.BlogMapper;
import com.example.blogging.repository.CategoryRepository;
import com.example.blogging.util.SlugUtil;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse create(CategoryRequest request) {
        String slug = SlugUtil.toSlug(request.name());
        if (categoryRepository.existsBySlug(slug)) {
            throw new BadRequestException("Category already exists: " + request.name());
        }

        Category category = new Category(request.name().trim(), slug, cleanOptional(request.description()));
        return BlogMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll(Sort.by("name")).stream()
                .map(BlogMapper::toCategoryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        return BlogMapper.toCategoryResponse(requireCategory(id));
    }

    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = requireCategory(id);
        String slug = SlugUtil.toSlug(request.name());
        categoryRepository.findBySlug(slug)
                .filter(existing -> !Objects.equals(existing.getId(), id))
                .ifPresent(existing -> {
                    throw new BadRequestException("Category already exists: " + request.name());
                });

        category.setName(request.name().trim());
        category.setSlug(slug);
        category.setDescription(cleanOptional(request.description()));
        return BlogMapper.toCategoryResponse(category);
    }

    public void delete(Long id) {
        Category category = requireCategory(id);
        categoryRepository.delete(category);
    }

    @Transactional(readOnly = true)
    public Category requireCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
    }

    private String cleanOptional(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
