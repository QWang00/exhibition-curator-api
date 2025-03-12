package com.northcoders.exhibition_curation_platform.service;

import com.northcoders.exhibition_curation_platform.model.Artwork;
import com.northcoders.exhibition_curation_platform.model.Exhibition;
import java.util.List;

public interface ExhibitionService {

    List<Exhibition> getAllExhibitions ();

    Exhibition getExhibitionById (Long id);

    Exhibition createExhibition(String name);

    Exhibition updateExhibitionNameById(Long exhibitionId, String newName);

    Exhibition addArtworkToExhibition(Long exhibitionId, String sourceId, String museum);

    Exhibition removeArtworkFromExhibition(Long exhibitionId, Long artworkId);

    Artwork fetchFromApi(String sourceId, String museum);

    Exhibition addArtworkToExhibitionFromLocal(Long exhibitionId, Long artworkId);
}
