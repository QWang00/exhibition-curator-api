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

    public List<Artwork> fetchArtworkList(String query, int limit, int skip, String artist) {
        StringBuilder urlBuilder = new StringBuilder(
                BASE_URL +
                        "/artworks/?has_image=1" +
                        "&q=" + query +
                        "&skip=" + skip +
                        "&limit=" + limit +
                        "&fields=tombstone,images");
        if (!artist.isEmpty()) {
            urlBuilder.append("&artists=").append(artist);
        }

        Map <String, Object> response = restTemplate.getForObject(urlBuilder.toString(), Map.class);

        // Check if response is null or doesn't contain "data"
        if (response == null || !response.containsKey("data")) {
            return Collections.emptyList();
        }

        // Get "data" field and check if it's empty
        List<Map<String, Object>> artworksData = (List<Map<String, Object>>) response.get("data");
        if (artworksData == null || artworksData.isEmpty()) {
            return Collections.emptyList();
        }

        return artworksData.stream()
                .map(this::mapToArtwork)
                .collect(Collectors.toList());
    }

    public Artwork fetchArtworkDetail(Long id, Artwork existingArtwork) {
        String url = BASE_URL + "artworks/" + id + "?fields=description,tombstone,images";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        // Ensure response and "data" exist
        if (response == null || !response.containsKey("data")) {
            return existingArtwork; // Return existing artwork unchanged if no data
        }

        Map<String, Object> artworkData = (Map<String, Object>) response.get("data");

        // Update description only if it's not null
        String description = (String) artworkData.get("description");
        if (description != null) {
            existingArtwork.setDescription(description);
        }

        return existingArtwork;
    }

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
