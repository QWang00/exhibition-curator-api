package com.northcoders.exhibition_curation_platform.controller;

import com.northcoders.exhibition_curation_platform.exception.GlobalExceptionHandler;
import com.northcoders.exhibition_curation_platform.exception.ItemNotFoundException;
import com.northcoders.exhibition_curation_platform.model.Exhibition;
import com.northcoders.exhibition_curation_platform.service.ExhibitionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ExhibitionControllerTest {

    @Mock
    private ExhibitionService exhibitionService;

    @InjectMocks
    private ExhibitionController exhibitionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(exhibitionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Nested
    class GetAllExhibitions {
        @Test
        @DisplayName("Should return empty list and 200 OK when no exhibitions exist")
        void getEmptyExhibitions() throws Exception {
            when(exhibitionService.getAllExhibitions()).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/api/v1/exhibitions"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isEmpty());
        }

        @Test
        @DisplayName("Should return exhibitions list and 200 OK when exhibitions exist")
        void getExhibitionsWithResults() throws Exception {
            Exhibition exhibition = Exhibition.builder().id(1L).name("Modern Art").build();
            when(exhibitionService.getAllExhibitions()).thenReturn(List.of(exhibition));

            mockMvc.perform(get("/api/v1/exhibitions"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].name").value("Modern Art"));
        }
    }


}