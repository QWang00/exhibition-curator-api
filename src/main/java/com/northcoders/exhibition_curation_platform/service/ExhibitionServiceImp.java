package com.northcoders.exhibition_curation_platform.service;

import com.northcoders.exhibition_curation_platform.exception.ItemNotFoundException;
import com.northcoders.exhibition_curation_platform.model.Artwork;
import com.northcoders.exhibition_curation_platform.model.Exhibition;
import com.northcoders.exhibition_curation_platform.repository.ArtworkRepository;
import com.northcoders.exhibition_curation_platform.repository.ExhibitionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ExhibitionServiceImp implements ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final ArtworkRepository artworkRepository;
    private final HarvardApiClient harvardApiClient;
    private final ClevelandApiClient clevelandApiClient;

    @Autowired
    public ExhibitionServiceImp(
            ExhibitionRepository exhibitionRepository,
            ArtworkRepository artworkRepository,
            HarvardApiClient harvardApiClient,
            ClevelandApiClient clevelandApiClient
    ) {
        this.exhibitionRepository = exhibitionRepository;
        this.artworkRepository = artworkRepository;
        this.harvardApiClient = harvardApiClient;
        this.clevelandApiClient = clevelandApiClient;
    }

    public List<Exhibition> getAllExhibitions() {
        List<Exhibition> exhibitions = new ArrayList<>();
        exhibitionRepository.findAll().forEach(exhibitions::add);
        return exhibitions;
    }

    public Exhibition getExhibitionById(Long id) {
        return exhibitionRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(String.format("The exhibition with id '%s' cannot be found", id)));
    }

    public Exhibition createExhibition(String name) {
        Exhibition exhibition = new Exhibition();
        exhibition.setName(name);
        return exhibitionRepository.save(exhibition);
    }

    public Exhibition updateExhibitionNameById(Long exhibitionId, String newName) {
        Exhibition exhibition = getExhibitionById(exhibitionId);
        exhibition.setName(newName);
        return exhibitionRepository.save(exhibition);
    }

    public Exhibition addArtworkToExhibition(Long exhibitionId, Integer sourceId, String museum) {
        Exhibition exhibition = getExhibitionById(exhibitionId);

        Artwork artwork = artworkRepository.findBySourceArtworkIdAndMuseumName(sourceId, museum)
                .orElseGet(() -> {
                    Artwork newArtwork = fetchFromApi(sourceId, museum);
                    return artworkRepository.save(newArtwork);
                });

        exhibition.addArtwork(artwork);
        return exhibitionRepository.save(exhibition);
    }



    public Artwork fetchFromApi(Integer sourceId, String museum) {
        return switch (museum) {
            case "Harvard Art Museum" -> harvardApiClient.fetchArtworkDetail(sourceId);
            case "The Cleveland Museum of Art" -> clevelandApiClient.fetchArtworkDetail(sourceId);
            default -> throw new IllegalArgumentException("Invalid museum");
        };
    }
}