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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
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
            .build();
    Artwork harvardArtwork2 = Artwork.builder()
            .title("title2 keyword")
            .artist("artist1")
            .museumName("Harvard Art Museum")
            .build();
    Artwork clevelandArtwork1 = Artwork.builder()
            .artist("artist2")
            .tombstone("keyword, yearMade3, artist3, artistCulture3, artistActive3")
            .museumName("The Cleveland Museum of Art")
            .build();
    Artwork clevelandArtwork2 = Artwork.builder()
            .artist("artist2")
            .tombstone("keyword, yearMade4, artist4, artistCulture4, artistActive4")
            .museumName("The Cleveland Museum of Art")
            .build();
    private final List<Artwork> harvardArtwork = List.of(harvardArtwork1, harvardArtwork2);
    private final List<Artwork> clevelandArtwork = List.of(clevelandArtwork1, clevelandArtwork2);

    private final String BASE_URL = "/api/v1";

    @Nested
    class GetArtworks {
        @Test
        @DisplayName("Should expect bad request when museum name is invalid ")
        void invalidMuseum() throws Exception {
            mockMvcController.perform(get(BASE_URL + "/search-results/invalidMuseum"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("Invalid museum name"));
        }

        @Test
        @DisplayName("Should return Harvard artworks when keyword match and artist field is empty")
        void harvardWithKeywordNoArtist() throws Exception {
            when (artworkService.getArtworks("keyword", null, "Harvard Art Museum",1))
                    .thenReturn(harvardArtwork);
            mockMvcController.perform(get(BASE_URL + "/search-results/harvard")
                    .param("keyword", "keyword")
                    .param("page", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nextPage").doesNotExist())
                    .andExpect(jsonPath("$.prevPage").doesNotExist())
                    .andExpect(jsonPath("$.artworks").isArray())
                    .andExpect(jsonPath("$.artworks.length()").value(2));
        }

        @Test
        @DisplayName("Should return Cleveland artworks when keyword match and artist field is empty")
        void clevelandWithKeywordNoArtist() throws Exception {
            when (artworkService.getArtworks("keyword", null, "The Cleveland Museum of Art",1))
                    .thenReturn(clevelandArtwork);
            mockMvcController.perform(get(BASE_URL + "/search-results/cleveland")
                            .param("keyword", "keyword")
                            .param("page", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.artworks").isArray())
                    .andExpect(jsonPath("$.artworks.length()").value(2));
        }

        @Test
        @DisplayName("Should return Harvard artworks when artist match and keyword field is empty")
        void harvardWithArtistNoKeyword() throws Exception {
            when (artworkService.getArtworks(null, "artist1", "Harvard Art Museum",1))
                    .thenReturn(harvardArtwork);
            mockMvcController.perform(get(BASE_URL + "/search-results/harvard")
                            .param("artist", "artist1")
                            .param("page", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.artworks").isArray())
                    .andExpect(jsonPath("$.artworks.length()").value(2));
        }

        @Test
        @DisplayName("Should return Cleveland artworks when artist match and keyword field is empty")
        void clevelandWithArtistNoKeyword() throws Exception {
            when (artworkService.getArtworks(null, "artist2", "The Cleveland Museum of Art",1))
                    .thenReturn(clevelandArtwork);
            mockMvcController.perform(get(BASE_URL + "/search-results/cleveland")
                            .param("artist", "artist2")
                            .param("page", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.artworks").isArray())
                    .andExpect(jsonPath("$.artworks.length()").value(2));
        }

        @Test
        @DisplayName("Should return artworks when both keyword and artist match")
        void harvardWithKeywordArtist() throws Exception {
            when (artworkService.getArtworks("keyword", "artist1", "Harvard Art Museum",1))
                    .thenReturn(harvardArtwork);
            mockMvcController.perform(get(BASE_URL + "/search-results/harvard")
                            .param("keyword", "keyword")
                            .param("artist", "artist1")
                            .param("page", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.artworks").isArray())
                    .andExpect(jsonPath("$.artworks.length()").value(2));
        }

        @Test
        @DisplayName("Should return artworks when both keyword and artist match")
        void clevelandWithKeywordArtist() throws Exception {
            when (artworkService.getArtworks("keyword", "artist2", "The Cleveland Museum of Art",1))
                    .thenReturn(clevelandArtwork);
            mockMvcController.perform(get(BASE_URL + "/search-results/cleveland")
                            .param("keyword", "keyword")
                            .param("artist", "artist2")
                            .param("page", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.artworks").isArray())
                    .andExpect(jsonPath("$.artworks.length()").value(2));
        }

        @Test
        @DisplayName("Should show next page when there are 11 results")
        void testNextPageWorkingWhenElevenResults() throws Exception {
            List<Artwork> artworks = new ArrayList<>();
            for (int i = 1; i <= 11; i++) {
                artworks.add(
                        Artwork.builder()
                                .title("Title" + i)
                                .artist("Artist" + i)
                                .museumName("The Cleveland Museum of Art")
                                .build()
                );
            }

            Mockito.when(artworkService.getArtworks(null, null, "The Cleveland Museum of Art", 1))
                    .thenReturn(artworks);

            mockMvcController.perform(get(BASE_URL + "/search-results/cleveland")
                            .param("page", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nextPage").value(2));
        }

        @Test
        @DisplayName("Should return no result when there is no match")
        void noMatch() throws Exception {
            when(artworkService.getArtworks("noMatch", "stillNoMatch", "The Cleveland Museum of Art", 1))
                    .thenReturn(Collections.emptyList());

            mockMvcController.perform(get(BASE_URL + "/search-results/cleveland")
                            .param("page", "1")
                            .param("keyword", "noMatch")
                            .param("artist", "stillNoMatch"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.artworks").isArray())
                    .andExpect(jsonPath("$.nextPage").doesNotExist())
                    .andExpect(jsonPath("$.prevPage").doesNotExist())
                    .andExpect(jsonPath("$.artworks.length()").value(0));
        }

    }

    @Nested
    class GetArtworkDetails {

        @Test
        @DisplayName("Should throw exception when museum name is invalid")
        void invalidMuseumName () throws Exception {
            mockMvcController.perform(get(BASE_URL + "/invalid/artwork/1234"))
                    .andExpect(status().isBadRequest());
        }
    }



}