package com.example.blogging.repository;

import com.example.blogging.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsBySlug(String slug);

    Optional<Tag> findBySlug(String slug);

    List<Tag> findByIdIn(Set<Long> ids);
}
