package com.northcoders.exhibition_curation_platform.service;

import com.northcoders.exhibition_curation_platform.model.Artwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service
public class ArtworkServiceImpl implements ArtworkService{

    private final HarvardApiClient harvardApiClient;
    private final ClevelandApiClient clevelandApiClient;

    @Autowired
    public ArtworkServiceImpl(HarvardApiClient harvardApiClient, ClevelandApiClient clevelandApiClient) {
        this.harvardApiClient = harvardApiClient;
        this.clevelandApiClient = clevelandApiClient;
    }

    @Override
    public List<Artwork> getArtworks(String keyword, String artist, String museum, int page) { // page starting from 1
        if ("Harvard Art Museum".equals(museum)) {
            return harvardApiClient.fetchArtworkList(keyword, artist, 11, page);
        } else if ("The Cleveland Museum of Art".equals(museum)) {
            return clevelandApiClient.fetchArtworkList(keyword, 11, (page-1) * 11, artist);
        }
        return Collections.emptyList();
    }

    @Override
    public Artwork getArtworkDetails(int sourceId, String museumName) {

        if("Harvard Art Museum".equalsIgnoreCase(museumName)) {
            return harvardApiClient.fetchArtworkDetail(sourceId);
        } else if ("The Cleveland Museum of Art".equalsIgnoreCase(museumName)) {
            return clevelandApiClient.fetchArtworkDetail(sourceId);
        } else {
            throw new IllegalArgumentException("Invalid museum source: " + museumName);
        }
    }
}
