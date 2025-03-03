package com.northcoders.exhibition_curation_platform.service;

import com.northcoders.exhibition_curation_platform.model.Artwork;

import java.util.List;

public interface ArtworkService {
    List<Artwork> getArtworks (String keyword, String artist, String museum, int page);

    Artwork getArtworkDetails (String sourceId, String museumName);
}
