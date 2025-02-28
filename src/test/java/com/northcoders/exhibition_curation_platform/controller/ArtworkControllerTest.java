package com.northcoders.exhibition_curation_platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.northcoders.exhibition_curation_platform.model.Artwork;
import com.northcoders.exhibition_curation_platform.service.ArtworkService;
import com.northcoders.exhibition_curation_platform.service.ArtworkServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ArtworkControllerTest {
    @Mock
    private ArtworkService mockArtworkService;

    @InjectMocks
    private ArtworkController artworkController;

    @Autowired
    private MockMvc mockMvcController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    Artwork harvardArtwork1 = Artwork.builder()
            .title("title1 keyword")
            .yearMade("1920")
            .culture("Germany")
            .artist("artist1")
            .artistActiveYear("1900-1980")
            .museumName("Harvard Art Museum")
            .sourceArtworkId(1234)
            .build();
    Artwork harvardArtwork2 = Artwork.builder()
            .title("title2 keyword")
            .yearMade("1930")
            .culture("British")
            .artist("artist2")
            .artistActiveYear("1980-1990")
            .museumName("Harvard Art Museum")
            .sourceArtworkId(1235)
            .build();
    Artwork clevelandArtwork1 = Artwork.builder()
            .tombstone("title3, yearmade3, artist3, artistCulture3, artistActive3")
            .museumName("The Cleveland Museum of Art")
            .sourceArtworkId(1236)
            .build();
    Artwork clevelandArtwork2 = Artwork.builder()
            .tombstone("title4, yearmade4, artist4, artistCulture4, artistActive4")
            .museumName("The Cleveland Museum of Art")
            .sourceArtworkId(1237)
            .build();
    private final List<Artwork> sampleArtworks = List.of(harvardArtwork1, harvardArtwork2, clevelandArtwork1, clevelandArtwork2);

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

    }



}