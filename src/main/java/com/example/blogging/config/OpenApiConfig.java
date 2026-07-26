package com.example.blogging.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bloggingApiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Blogging API")
                        .version("1.0.0")
                        .description("REST API for managing users, posts, comments, categories, and tags.")
                        .contact(new Contact()
                                .name("Example Team")
                                .email("support@example.com")));
    }
}
