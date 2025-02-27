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


    }
}