package com.example.blogging.config;

import com.example.blogging.domain.Category;
import com.example.blogging.domain.Comment;
import com.example.blogging.domain.Post;
import com.example.blogging.domain.PostStatus;
import com.example.blogging.domain.Tag;
import com.example.blogging.domain.User;
import com.example.blogging.repository.CategoryRepository;
import com.example.blogging.repository.CommentRepository;
import com.example.blogging.repository.PostRepository;
import com.example.blogging.repository.TagRepository;
import com.example.blogging.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            TagRepository tagRepository,
            PostRepository postRepository,
            CommentRepository commentRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }

            User demoUser = userRepository.save(new User(
                    "Demo Author",
                    "demo@example.com",
                    passwordEncoder.encode("password"),
                    "Writes about building useful software."
            ));

            Category engineering = categoryRepository.save(new Category(
                    "Engineering",
                    "engineering",
                    "Technical articles and implementation notes."
            ));
            Category product = categoryRepository.save(new Category(
                    "Product",
                    "product",
                    "Ideas about product design and strategy."
            ));

            Tag spring = tagRepository.save(new Tag("Spring Boot", "spring-boot"));
            Tag java = tagRepository.save(new Tag("Java", "java"));
            Tag api = tagRepository.save(new Tag("API", "api"));

            Post welcome = new Post(
                    "Welcome to the Blog",
                    "welcome-to-the-blog",
                    "A short tour of the demo blogging API.",
                    "This sample post is created on startup so you can test the API immediately.",
                    PostStatus.PUBLISHED,
                    demoUser,
                    engineering
            );
            welcome.setTags(Set.of(spring, java, api));
            postRepository.save(welcome);

            Post roadmap = new Post(
                    "What Makes a Helpful Blog Backend",
                    "what-makes-a-helpful-blog-backend",
                    "Notes on the API features a content site usually needs.",
                    "Posts, categories, tags, users, comments, search, pagination, and validation form the core of this backend.",
                    PostStatus.PUBLISHED,
                    demoUser,
                    product
            );
            roadmap.setTags(Set.of(api));
            postRepository.save(roadmap);

            commentRepository.save(new Comment(
                    "First Reader",
                    "reader@example.com",
                    "The seeded content makes the API easy to try.",
                    welcome
            ));
        };
    }
}
