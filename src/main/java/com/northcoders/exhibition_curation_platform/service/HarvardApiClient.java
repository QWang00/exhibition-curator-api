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
