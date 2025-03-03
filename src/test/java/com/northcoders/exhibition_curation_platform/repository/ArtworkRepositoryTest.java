package com.northcoders.exhibition_curation_platform.repository;

import com.northcoders.exhibition_curation_platform.model.Artwork;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ArtworkRepositoryTest {
    @Autowired
    private ArtworkRepository artworkRepository;

    @Nested
    class FindBySourceArtworkIdAndMuseumName {

        @Test
        @DisplayName("Should return valid artwork when parameters are valid")
        void validParameters () throws Exception {
            Artwork artwork = Artwork.builder()
                    .sourceArtworkId("100")
                    .museumName("Harvard Art Museum")
                    .imageUrl("image")
                    .build();
            artworkRepository.save(artwork);

            Optional<Artwork> artworkFound = artworkRepository.findBySourceArtworkIdAndMuseumName("100", "Harvard Art Museum");
            assertTrue(artworkFound.isPresent());
            assertEquals("100", artworkFound.get().getSourceArtworkId());
            assertEquals("Harvard Art Museum", artworkFound.get().getMuseumName());
        }

        @Test
        @DisplayName("Should return empty artwork when sourceId is invalid")
        void findBySourceArtworkIdAndMuseumName_InvalidSourceId_ReturnsEmpty() {
            Optional<Artwork> foundArtwork = artworkRepository
                    .findBySourceArtworkIdAndMuseumName("999", "Louvre");

            assertTrue(foundArtwork.isEmpty());
        }

        @Test
        @DisplayName("Should return empty artwork when museum name is invalid")
        void findBySourceArtworkIdAndMuseumName_InvalidMuseum_ReturnsEmpty() {
            Optional<Artwork> foundArtwork = artworkRepository
                    .findBySourceArtworkIdAndMuseumName("100", "Invalid Museum");

            assertTrue(foundArtwork.isEmpty());
        }
    }

}
