package com.northcoders.exhibition_curation_platform.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SeedDatabase {

    @Autowired
    private SeedService seedService;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            seedService.seedData();
        };
    }
}