package com.northcoders.exhibition_curation_platform.service;

import com.northcoders.exhibition_curation_platform.model.Artwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClevelandApiClient {
    private static final String BASE_URL = "https://openaccess-api.clevelandart.org/api";
    private static final String MUSEUM_NAME = "The Cleveland Museum of Art";

    @Autowired
    private RestTemplate restTemplate;



    private Artwork mapToArtwork(Map<String, Object> response) {
        Artwork artwork = new Artwork();
        artwork.setTombstone((String) response.get("tombstone"));

        // Extract images
        Map<String, Object> images = (Map<String, Object>) response.get("images");
        if (images != null) {
            Map<String, String> imageMap = null;

            // Prefer "print" image first
            if (images.containsKey("print")) {
                imageMap = (Map<String, String>) images.get("print");
            }
            // Fallback to "web" image if "print" is missing
            else if (images.containsKey("web")) {
                imageMap = (Map<String, String>) images.get("web");
            }

            // Set image URL if available
            if (imageMap != null) {
                artwork.setImageUrl(imageMap.get("url"));
            }
        }

        artwork.setMuseumName(MUSEUM_NAME);
        return artwork;
    }


}
