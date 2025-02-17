package com.northcoders.exhibition_curation_platform.config;

import io.github.cdimascio.dotenv.Dotenv;

public class ApiKeyManager {
    private static final Dotenv dotenv = Dotenv.configure().load();

    // get api key for Harvard Art Museum
    public static String getHAMApiKey() {
        return dotenv.get("harvard_museum_api_key");
    }
}
