package com.northcoders.exhibition_curation_platform.service;

import com.northcoders.exhibition_curation_platform.config.ApiKeyManager;
import com.northcoders.exhibition_curation_platform.model.Artwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HarvardApiClient {
    private static final String BASE_URL = "https://api.harvardartmuseums.org/object";
    private static final String MUSEUM_NAME = "Harvard Art Museum";

    private final RestTemplate restTemplate;

    @Autowired
    public HarvardApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Artwork> fetchArtworkList (String query, String artist, int size, int page){
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append("?fields=title,people,dated,primaryimageurl");

        if (query != null && !query.isEmpty()){
            urlBuilder.append("&q=").append(query);
        }

        if (artist!= null && !artist.isEmpty()){
            urlBuilder.append("&person=").append(artist);
        }

        urlBuilder.append("&size=").append(size)
                .append("&page=").append(page)
                .append("&hasimage=1")
                .append("&apikey=").append(ApiKeyManager.getHAMApiKey());
       String url = urlBuilder.toString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        // Check if response is null or doesn't contain "data"
        if (response == null || !response.containsKey("records")) {
            return Collections.emptyList();
        }

        // Get "data" field and check if it's empty
        List<Map<String, Object>> artworksData = (List<Map<String, Object>>) response.get("records");
        if (artworksData == null || artworksData.isEmpty()) {
            return Collections.emptyList();
        }

        return artworksData.stream()
                .filter(artworkData -> {
                    Integer imageLevel = (Integer) artworkData.get("imagepermissionlevel");
                    String imageUrl = (String) artworkData.get("primaryimageurl");
                    return imageLevel != 2 && imageUrl != null;
                })
                .map(this::mapToArtwork)
                .collect(Collectors.toList());

    }

    public Artwork fetchArtworkDetail (int id, Artwork existingArtwork){
        String url = BASE_URL +
                "/" + id +
                "?apikey=" + ApiKeyManager.getHAMApiKey();
        Map <String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null ) {
            return existingArtwork;
        }

        List<Map<String, Object>> images = (List<Map<String, Object>>) response.get("images");
        if (images != null && !images.isEmpty()) {
            for (Map<String, Object> image : images) {
                String description = (String) image.get("description");
                if (description != null) {
                    existingArtwork.setDescription(description);
                    return existingArtwork;
                }
            }
        }

        if (response.get("technique") != null) {
            existingArtwork.setDescription("Technique: " + response.get("technique"));
        } else if (response.get("medium") != null){
            existingArtwork.setDescription("Medium: " + response.get("medium"));
        } else if (response.get("classification") != null) {
            existingArtwork.setDescription("Classification: " + response.get("classification"));
        }

        return existingArtwork;

    }

    private Artwork mapToArtwork(Map<String, Object> response) {
        Artwork artwork = new Artwork();

        artwork.setTombstone((String) response.get("title"));
        artwork.setImageUrl((String) response.get("primaryimageurl") );
        artwork.setYearMade((String) response.get("dated"));
        artwork.setSourceArtworkId((Integer)response.get("id"));

        List<Map<String, Object>> people = (List<Map<String, Object>>) response.get("people");
        if (people != null && !people.isEmpty()) {
            Map<String, Object> firstPerson = people.get(0);
            artwork.setArtist((String) firstPerson.get("displayname"));
            artwork.setArtistActiveYear((String) firstPerson.get("displaydate"));
            artwork.setCulture((String) firstPerson.get("culture"));
        }
        artwork.setMuseumName(MUSEUM_NAME);
        return artwork;
    }

}
