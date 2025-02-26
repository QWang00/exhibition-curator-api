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
    public List<Artwork> getArtworks(String keyword, String artist, String museum, int page) {
        if ("Harvard".equals(museum)) {
            return harvardApiClient.fetchArtworkList(keyword, artist, 10, page);
        } else if ("Cleveland".equals(museum)) {
            return clevelandApiClient.fetchArtworkList(keyword, 10, page * 10, artist);
        }
        return Collections.emptyList();
    }

    @Override
    public Artwork getArtworkDetails(Artwork existingArtwork) {
        return null;
    }
}
