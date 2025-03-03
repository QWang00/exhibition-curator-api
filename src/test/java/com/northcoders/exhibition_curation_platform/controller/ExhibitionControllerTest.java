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

    @Nested
    class GetExhibitionById {
        @Test
        @DisplayName("Should return exhibition and 200 OK when valid ID")
        void getExhibitionWithValidId() throws Exception {
            Exhibition exhibition = Exhibition.builder().id(1L).name("Classic Art").build();
            when(exhibitionService.getExhibitionById(1L)).thenReturn(exhibition);

            mockMvc.perform(get("/api/v1/exhibition/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("Classic Art"));
        }

        @Test
        @DisplayName("Should return 404 Not Found when invalid ID")
        void getExhibitionWithInvalidId() throws Exception {
            when(exhibitionService.getExhibitionById(999L))
                    .thenThrow(new ItemNotFoundException("The exhibition with id 999 cannot be found"));

            mockMvc.perform(get("/api/v1/exhibition/999"))
                    .andExpect(status().isNotFound())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").value("The exhibition with id 999 cannot be found"));        }
    }

    @Nested
    class CreateExhibition {
        @Test
        @DisplayName("Should create exhibition and return 201 Created")
        void createExhibitionSuccessfully() throws Exception {
            Exhibition created = Exhibition.builder().id(1L).name("Renaissance").build();
            when(exhibitionService.createExhibition("Renaissance")).thenReturn(created);

            mockMvc.perform(post("/api/v1/exhibitions")
                            .param("name", "Renaissance"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("Renaissance"));
        }

        @Test
        @DisplayName("Should return 400 Bad Request when missing name parameter")
        void createExhibitionWithoutName() throws Exception {
            mockMvc.perform(post("/api/v1/exhibitions"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class UpdateExhibitionName {
        @Test
        @DisplayName("Should update name and return 200 OK")
        void updateNameSuccessfully() throws Exception {
            Exhibition updated = Exhibition.builder().id(1L).name("New Name").build();
            when(exhibitionService.updateExhibitionNameById(1L, "New Name")).thenReturn(updated);

            mockMvc.perform(put("/api/v1/exhibition/1/name")
                            .param("newName", "New Name"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("New Name"));
        }

        @Test
        @DisplayName("Should return 404 Not Found when invalid ID")
        void updateNameWithInvalidId() throws Exception {
            when(exhibitionService.updateExhibitionNameById(999L, "New Name"))
                    .thenThrow(new ItemNotFoundException("Exhibition not found"));

            mockMvc.perform(put("/api/v1/exhibition/999/name")
                            .param("newName", "New Name"))
                    .andExpect(status().isNotFound());
        }
    }


}