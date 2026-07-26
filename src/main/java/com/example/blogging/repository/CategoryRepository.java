package com.example.blogging.repository;

import com.example.blogging.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsBySlug(String slug);

    Optional<Category> findBySlug(String slug);
}
