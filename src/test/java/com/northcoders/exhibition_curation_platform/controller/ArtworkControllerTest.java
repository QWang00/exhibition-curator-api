package com.northcoders.exhibition_curation_platform.controller;

import com.northcoders.exhibition_curation_platform.model.Artwork;
import com.northcoders.exhibition_curation_platform.service.ArtworkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class ArtworkControllerTest {

    @Mock
    private ArtworkService artworkService;

    @InjectMocks
    private ArtworkController artworkController;

    private MockMvc mockMvcController;

    @BeforeEach
    void setup() {
        mockMvcController = MockMvcBuilders.standaloneSetup(artworkController)
                .build();
    }

    Artwork harvardArtwork1 = Artwork.builder()
            .title("title1 keyword")
            .artist("artist1")
            .museumName("Harvard Art Museum")
            .sourceArtworkId(1234)
            .build();
    Artwork harvardArtwork2 = Artwork.builder()
            .title("title2 keyword")
            .artist("artist1")
            .museumName("Harvard Art Museum")
            .sourceArtworkId(1235)
            .build();
    Artwork clevelandArtwork1 = Artwork.builder()
            .artist("artist2")
            .tombstone("keyword, yearmade3, artist3, artistCulture3, artistActive3")
            .museumName("The Cleveland Museum of Art")
            .sourceArtworkId(1236)
            .build();
    Artwork clevelandArtwork2 = Artwork.builder()
            .artist("artist2")
            .tombstone("keyword, yearmade4, artist4, artistCulture4, artistActive4")
            .museumName("The Cleveland Museum of Art")
            .sourceArtworkId(1237)
            .build();
    private final List<Artwork> harvardArtwork = List.of(harvardArtwork1, harvardArtwork2);
    private final List<Artwork> clevelandArtwork = List.of(clevelandArtwork1, clevelandArtwork2);

    private final String BASE_URL = "/api/v1/search-results";

    @Nested
    class GetArtworks {
        @Test
        @DisplayName("Should expect bad request when museum name is invalid ")
        void invalidMuseum() throws Exception {
            mockMvcController.perform(get(BASE_URL + "/invalidMuseum"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("Invalid museum name"));
        }

        @Test
        @DisplayName("Should return Harvard artworks when keyword match and artist field is empty")
        void harvardWithKeywordNoArtist() throws Exception {
            when (artworkService.getArtworks("keyword", null, "Harvard Art Museum",1))
                    .thenReturn(harvardArtwork);
            mockMvcController.perform(get(BASE_URL + "/harvard")
                    .param("keyword", "keyword")
                    .param("page", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.artworks").isArray())
                    .andExpect(jsonPath("$.artworks.length()").value(2));
        }

        @Test
        @DisplayName("Should return Cleveland artworks when keyword match and artist field is empty")
        void clevelandWithKeywordNoArtist() throws Exception {
            when (artworkService.getArtworks("keyword", null, "The Cleveland Museum of Art",1))
                    .thenReturn(clevelandArtwork);
            mockMvcController.perform(get(BASE_URL + "/cleveland")
                            .param("keyword", "keyword")
                            .param("page", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.artworks").isArray())
                    .andExpect(jsonPath("$.artworks.length()").value(2));
        }

    }



}