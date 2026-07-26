package com.example.blogging.repository;

import com.example.blogging.domain.Post;
import com.example.blogging.domain.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    boolean existsBySlug(String slug);

    Optional<Post> findBySlug(String slug);

    @Query("""
            select distinct p from Post p
            left join p.tags t
            where (:status is null or p.status = :status)
              and (:authorId is null or p.author.id = :authorId)
              and (:categoryId is null or p.category.id = :categoryId)
              and (:tagId is null or t.id = :tagId)
              and (:query is null or lower(p.title) like lower(concat('%', :query, '%'))
                   or lower(p.excerpt) like lower(concat('%', :query, '%')))
            """)
    Page<Post> search(
            @Param("query") String query,
            @Param("status") PostStatus status,
            @Param("authorId") Long authorId,
            @Param("categoryId") Long categoryId,
            @Param("tagId") Long tagId,
            Pageable pageable
    );
}
