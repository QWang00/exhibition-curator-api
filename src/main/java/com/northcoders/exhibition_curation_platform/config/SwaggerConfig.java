package com.northcoders.exhibition_curation_platform.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI exhibitionCuratorAPI() {
        return new OpenAPI()
                .info(new Info().title("Exhibition Curator")
                        .description("Spring Boot REST API for 'Exhibition Curator' application")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
