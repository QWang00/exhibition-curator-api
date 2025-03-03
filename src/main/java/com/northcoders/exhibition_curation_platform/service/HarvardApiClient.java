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
                .filter(this::isValidArtwork)
                .map(this::mapToArtwork)
                .collect(Collectors.toList());

    }

    public Artwork fetchArtworkDetail (String id){
        String url = BASE_URL +
                "/" + id +
                "?apikey=" + ApiKeyManager.getHAMApiKey();
        Map <String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null ) {
            return null;
        }

        Artwork artwork = new Artwork();
        artwork.setSourceArtworkId(id);
        artwork.setMuseumName(MUSEUM_NAME);

        mapCommonFields(response, artwork);
        setArtistInfo(response, artwork);
        setDescription(response, artwork);
        generatePreview(artwork);

        return artwork;

    }

    private Artwork mapToArtwork(Map<String, Object> artworkData) {
        Artwork artwork = new Artwork();
        artwork.setTitle((String) artworkData.get("title"));
        artwork.setImageUrl((String) artworkData.get("primaryimageurl"));
        artwork.setYearMade((String) artworkData.get("dated"));
        artwork.setSourceArtworkId((String.valueOf(artworkData.get("id"))) );

        setArtistInfo(artworkData, artwork);
        generatePreview(artwork);

        return artwork;
    }

    private void mapCommonFields(Map<String, Object> response, Artwork artwork) {
        artwork.setTitle((String) response.get("title"));
        artwork.setImageUrl((String) response.get("primaryimageurl"));
        artwork.setYearMade((String) response.get("dated"));
    }

    private void setArtistInfo(Map<String, Object> source, Artwork artwork) {
        List<Map<String, Object>> people = (List<Map<String, Object>>) source.get("people");
        if (people != null && !people.isEmpty()) {
            Map<String, Object> firstPerson = people.get(0);
            artwork.setArtist((String) firstPerson.get("displayname"));
            artwork.setArtistActiveYear((String) firstPerson.get("displaydate"));
            artwork.setCulture((String) firstPerson.get("culture"));
        }
    }

    private void setDescription(Map<String, Object> response, Artwork artwork) {
        String description = extractDescriptionFromImages(response);

        if (description == null) {
            description = getFallbackDescription(response);
        }

        artwork.setDescription(description);
    }

    private String extractDescriptionFromImages(Map<String, Object> response) {
        List<Map<String, Object>> images = (List<Map<String, Object>>) response.get("images");
        if (images != null) {
            for (Map<String, Object> image : images) {
                String desc = (String) image.get("description");
                if (desc != null) return desc;
            }
        }
        return null;
    }

    private String getFallbackDescription(Map<String, Object> response) {
        if (response.get("technique") != null) return "Technique: " + response.get("technique");
        if (response.get("medium") != null) return "Medium: " + response.get("medium");
        if (response.get("classification") != null) return "Classification: " + response.get("classification");
        return null;
    }

    private void generatePreview(Artwork artwork) {
        String preview = String.format("%s, %s. %s (%s, %s).",
                artwork.getTitle(),
                artwork.getYearMade(),
                artwork.getArtist(),
                artwork.getCulture(),
                artwork.getArtistActiveYear());
        artwork.setPreview(preview);
    }

    private boolean isValidArtwork(Map<String, Object> artworkData) {
        Integer imageLevel = (Integer) artworkData.get("imagepermissionlevel");
        String imageUrl = (String) artworkData.get("primaryimageurl");
        return imageLevel != null && imageLevel != 2 && imageUrl != null;
    }

}
