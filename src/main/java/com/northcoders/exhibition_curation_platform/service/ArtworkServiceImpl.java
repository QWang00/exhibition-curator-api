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
            return harvardApiClient.fetchArtworkList(keyword, artist, 10, page);
        } else if ("The Cleveland Museum of Art".equals(museum)) {
            return clevelandApiClient.fetchArtworkList(keyword, 10, (page-1) * 10, artist);
        }
        return Collections.emptyList();
    }

    @Override
    public Artwork getArtworkDetails(Artwork existingArtwork) {
        int sourceArtworkId = existingArtwork.getSourceArtworkId();
        String museumName = existingArtwork.getMuseumName();
        if("Harvard Art Museum".equals(museumName)) {
            return harvardApiClient.fetchArtworkDetail(sourceArtworkId, existingArtwork);
        } else if ("The Cleveland Museum of Art".equals(museumName)) {
            return clevelandApiClient.fetchArtworkDetail(sourceArtworkId, existingArtwork);
        } else {
            throw new IllegalArgumentException("Invalid museum source: " + museumName);
        }
    }
}
