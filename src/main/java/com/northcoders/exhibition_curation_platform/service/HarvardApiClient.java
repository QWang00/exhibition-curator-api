package com.northcoders.exhibition_curation_platform.service;

import com.northcoders.exhibition_curation_platform.config.ApiKeyManager;
import com.northcoders.exhibition_curation_platform.model.Artwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HarvardApiClient {
    private static final String BASE_URL = "https://api.harvardartmuseums.org/object";
    private static final String MUSEUM_NAME = "Harvard Art Museum";

    @Autowired
    private RestTemplate restTemplate;

    // Fetch list of artworks (search results)
    public List<Artwork> fetchArtworkList (String query, String artist, int size, int page){
        String url = BASE_URL +
                "?fields=title,people,dataend,primaryimageurl"  +
                "&q=" + query +
                "&person=" + artist +
                "&size=" + size +
                "&page=" + page +
                "&hasimage=1" +
                "&apikey=" + ApiKeyManager.getHAMApiKey();
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        List<Map<String, Object>> artworksData = (List<Map<String, Object>>) response.get("data");

        return artworksData.stream()
                .filter(artworkData -> {
                    Integer imageLevel = (Integer) artworkData.get("imagepermissionlevel");
                    return imageLevel != 2;
                })
                .map(this::mapToArtworkList)
                .collect(Collectors.toList());

    }

    // Fetch artwork detail
    public Artwork fetchArtworkDetail (Long id, Artwork existingArtwork){
        String url = BASE_URL +
                "/" + id +
                "?apikey=" + ApiKeyManager.getHAMApiKey();
        Map <String, Object> response = restTemplate.getForObject(url, Map.class);
        Map<String, Object> artworkData = (Map<String, Object>) response.get("data");
        existingArtwork.setDescription((String)((List<Map<String, Object>>) artworkData.get("images")).get(0).get("description"));
        return existingArtwork;
    }
    private Artwork mapToArtworkList(Map<String, Object> response) {
        Artwork artwork = new Artwork();
        artwork.setTombstone((String) response.get("title"));
        artwork.setImageUrl((String) response.get("primaryimageurl") );
        artwork.setYearMade((String) response.get("dateend"));

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
