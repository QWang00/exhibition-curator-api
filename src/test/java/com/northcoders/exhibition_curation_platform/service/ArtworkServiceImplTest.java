package com.northcoders.exhibition_curation_platform.service;

import com.northcoders.exhibition_curation_platform.model.Artwork;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArtworkServiceImplTest {

    @Mock
    private HarvardApiClient harvardApiClient;

    @Mock
    private ClevelandApiClient clevelandApiClient;

    @InjectMocks
    private ArtworkServiceImpl artworkService;

    @BeforeEach
    void setUp() {
    }

    @Nested
    class GetArtworks {

        @Test
        @DisplayName("Should return an empty list when museum name not found")
        void invalidMuseum(){
            List<Artwork> actualResult = artworkService.getArtworks("keyword", "artist", "Invalid", 1);
            assertThat(actualResult.isEmpty());
        }

        @Test
        @DisplayName("Should fetch artwork list from Harvard API when 'Harvard' is specified as the museum")
        void harvardMuseum(){
            List<Artwork> artworks = new ArrayList<>();
            artworks.add(Artwork.builder()
                    .title("title keyword")
                    .yearMade("1920")
                    .culture("Germany")
                    .artist("artist")
                    .artistActiveYear("1900-1980")
                    .museumName("Harvard Art Museum")
                    .build());

            when(harvardApiClient.fetchArtworkList("keyword", "artist", 10,1)).thenReturn(artworks);
            List<Artwork> actualResult = artworkService.getArtworks("keyword", "artist", "Harvard Art Museum", 1);
            assertThat(actualResult.size() == 1);
            assertThat(actualResult.getFirst().getMuseumName().equals("Harvard Art Museum"));
            assertThat(actualResult.getFirst().getCulture().equals("Germany"));
            verify(harvardApiClient).fetchArtworkList("keyword", "artist", 10, 1);

        }

        @Test
        @DisplayName("Should fetch artwork list from Cleveland API when it is specified as the museum")
        void clevelandMuseum(){
            List<Artwork> artworks = new ArrayList<>();
            artworks.add(Artwork.builder()
                    .tombstone("keyword tombstone, 1920, Germany, artist, 1900-1980")
                    .museumName("The Cleveland Museum of Art")
                    .artist("artist")
                    .build());

            when(clevelandApiClient.fetchArtworkList("keyword", 10, 0, "artist")).thenReturn(artworks);
            List<Artwork> actualResult = artworkService.getArtworks("keyword", "artist", "The Cleveland Museum of Art", 1);
            assertThat(actualResult.size() == 1);
            assertThat(actualResult.getFirst().getTombstone().equals("keyword tombstone, 1920, Germany, artist, 1900-1980"));
            verify(clevelandApiClient).fetchArtworkList("keyword", 10, 0, "artist");

        }
    }
}