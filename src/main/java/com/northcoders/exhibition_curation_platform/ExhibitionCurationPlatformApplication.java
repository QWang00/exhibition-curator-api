package com.northcoders.exhibition_curation_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class ExhibitionCurationPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExhibitionCurationPlatformApplication.class, args);
	}

}
