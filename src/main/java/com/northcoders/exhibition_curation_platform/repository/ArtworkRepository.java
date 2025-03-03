package com.northcoders.exhibition_curation_platform.repository;

import com.northcoders.exhibition_curation_platform.model.Artwork;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ArtworkRepository extends CrudRepository<Artwork,Long> {
    Optional<Artwork> findBySourceArtworkIdAndMuseumName(String sourceArtworkId, String museumName);
}
