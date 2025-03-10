package com.northcoders.exhibition_curation_platform.config;

import com.northcoders.exhibition_curation_platform.model.Artwork;
import com.northcoders.exhibition_curation_platform.model.Exhibition;
import com.northcoders.exhibition_curation_platform.repository.ArtworkRepository;
import com.northcoders.exhibition_curation_platform.repository.ExhibitionRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;

@Service
public class SeedService {

    @Autowired
    private ArtworkRepository artworkRepo;

    @Autowired
    private ExhibitionRepository exhibitionRepo;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public void seedData() {
        Set<Artwork> artworks = SeedData.createArtworks();
        artworkRepo.saveAll(artworks);

        artworks.forEach(artwork -> entityManager.merge(artwork));
        Set<Exhibition> exhibitions = SeedData.createExhibitions(artworks);
        exhibitionRepo.saveAll(exhibitions);
    }
}
