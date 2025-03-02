package com.northcoders.exhibition_curation_platform.service;

import com.northcoders.exhibition_curation_platform.exception.ItemNotFoundException;
import com.northcoders.exhibition_curation_platform.model.Artwork;
import com.northcoders.exhibition_curation_platform.model.Exhibition;
import com.northcoders.exhibition_curation_platform.repository.ExhibitionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ExhibitionServiceImplTest {

    @Mock
    private HarvardApiClient harvardApiClient;

    @Mock
    private ClevelandApiClient clevelandApiClient;

    @Mock
    private ExhibitionRepository mockExhibitionRepository;

    @InjectMocks
    private ExhibitionServiceImp exhibitionServiceImp;

    @Autowired
    private ExhibitionRepository exhibitionRepository;

    @Nested
    class GetAllExhibitions{

        @Test
        @DisplayName("Should return empty list when non exist")
        void noExhibitions () throws Exception {
            List<Exhibition> exhibitions = new ArrayList<>();
            when(mockExhibitionRepository.findAll())
                    .thenReturn(exhibitions);
            List<Exhibition> actual = exhibitionServiceImp.getAllExhibitions();
            assertThat(actual).isEmpty();
            assertThat(actual).isEqualTo(exhibitions);
        }

        @Test
        @DisplayName("Should return a list with one exhibition when there is only one")
            void oneArtwork () throws Exception {
            List<Exhibition> exhibitions = List.of(
                    Exhibition.builder()
                            .name("Monet")
                            .build()
            );
            when(mockExhibitionRepository.findAll()).thenReturn(exhibitions);
            List<Exhibition> actual = exhibitionServiceImp.getAllExhibitions();
            assertThat(actual).hasSize(1);
            assertThat(actual).isEqualTo(exhibitions);
        }

        @Test
        @DisplayName("Should return a list with multiple exhibitions when there are multiple")
        void multiArtwork () throws Exception {
            List<Exhibition> exhibitions = List.of(
                    Exhibition.builder()
                            .name("Monet")
                            .build(),
                    Exhibition.builder()
                            .name("Van")
                            .build()
            );
            when(mockExhibitionRepository.findAll()).thenReturn(exhibitions);
            List<Exhibition> actual = exhibitionServiceImp.getAllExhibitions();
            assertThat(actual).hasSize(2);
            assertThat(actual).isEqualTo(exhibitions);
        }

    }

    @Nested
    class GetExhibitionById {

        @Test
        @DisplayName("Should throw exception when ID is invalid")
        void idNotFound() throws Exception {
            List<Exhibition> exhibitions = List.of(
                    Exhibition.builder()
                            .name("Monet")
                            .build()
            );
            when(mockExhibitionRepository.findById(2L)).thenReturn(Optional.empty());
            assertThatThrownBy(()-> exhibitionServiceImp.getExhibitionById(2L))
                    .isInstanceOf(ItemNotFoundException.class)
                    .hasMessage(String.format("The exhibition with id '%s' cannot be found", 2));
            verify(mockExhibitionRepository, never()).save(any(Exhibition.class));
        }

        @Test
        @DisplayName("Should return exhibition when ID is valid")
        void IdFound() throws Exception {
            Exhibition exhibition = Exhibition.builder()
                    .name("Monet")
                    .build();
            List<Exhibition> exhibitions = List.of(exhibition);
            when(mockExhibitionRepository.findById(1L)).thenReturn(Optional.of(exhibition));
            Exhibition actual = exhibitionServiceImp.getExhibitionById(1L);
            assertThat(actual).isEqualTo(exhibition);
            assertThat(actual.getName()).isEqualTo("Monet");
        }
    }

    @Nested
    class CreateExhibition{

        @Test
        @DisplayName("Should add a new exhibition successfully ")
        void successCreation () throws Exception {
            Exhibition exhibition = Exhibition.builder()
                    .name("Monet")
                    .build();
            when(mockExhibitionRepository.save(exhibition)).thenReturn(exhibition);
            Exhibition actual = exhibitionServiceImp.createExhibition("Monet");
            assertThat(actual.getName()).isEqualTo("Impressionist Art");
            verify(mockExhibitionRepository, times(1)).save(any(Exhibition.class));
        }

        @Test
        @DisplayName("Should create exhibitions with duplicate name")
        void duplicateName () {
            Exhibition exhibition1 = Exhibition.builder()
                    .name("Monet")
                    .build();
            Exhibition exhibition2 = Exhibition.builder()
                    .name("Monet")
                    .build();

            when(mockExhibitionRepository.save(any(Exhibition.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Exhibition savedExhibition1 = exhibitionServiceImp.createExhibition("Monet");
            Exhibition savedExhibition2 = exhibitionServiceImp.createExhibition("Monet");

            assertThat(savedExhibition1).isNotNull();
            assertThat(savedExhibition2).isNotNull();
            assertThat(savedExhibition1.getName()).isEqualTo("Monet");
            assertThat(savedExhibition2.getName()).isEqualTo("Monet");
            verify(mockExhibitionRepository, times(2)).save(any(Exhibition.class));
        }

    }

    @Nested
    class UpdateExhibitionNameById {

        @Test
        @DisplayName("Should throw exception when ID is not found")
        void invalidId () throws Exception{
            when(mockExhibitionRepository.findById(2L)).thenReturn(Optional.empty());
            assertThatThrownBy(()-> exhibitionServiceImp.updateExhibitionNameById(2L, "name"))
                    .isExactlyInstanceOf(ItemNotFoundException.class)
                    .hasMessage("The exhibition with id '2' cannot be found");
        }

        @Test
        @DisplayName("Should update name successfully when ID is valid")
        void validId () throws Exception{
            Exhibition exhibition = Exhibition.builder()
                    .name("monet").build();
            when(mockExhibitionRepository.findById(1L)).thenReturn(Optional.of(exhibition));
            when(mockExhibitionRepository.save(any(Exhibition.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Exhibition actual = exhibitionServiceImp.updateExhibitionNameById(1L, "new");
            assertThat(actual).isNotNull();
            assertThat(actual.getName()).isEqualTo("New Name");

            verify(mockExhibitionRepository, times(1)).findById(1L);
            verify(mockExhibitionRepository, times(1)).save(exhibition);
        }

        @Test
        @DisplayName("Should update name successfully when new name is duplicate")
        void duplicateNewName () throws Exception{
            String duplicateName = "identical";
            Exhibition exhibition = Exhibition.builder()
                    .name(duplicateName).build();
            when(mockExhibitionRepository.findById(1L)).thenReturn(Optional.of(exhibition));
            when(mockExhibitionRepository.save(any(Exhibition.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Exhibition actual = exhibitionServiceImp.updateExhibitionNameById(1L, duplicateName);
            assertThat(actual).isNotNull();
            assertThat(actual.getName()).isEqualTo(duplicateName);

            verify(mockExhibitionRepository, times(1)).findById(1L);
            verify(mockExhibitionRepository, times(1)).save(exhibition);
        }

    }

    @Nested
    class FetchFromApi {

        @Test
        @DisplayName("Should throw exception when museum name is invalid")
        void invalidMuseum () throws Exception {
            Integer sourceId = 789;
            String invalidMuseum = "Unknown";

            assertThatThrownBy(() -> exhibitionServiceImp.fetchFromApi(sourceId, invalidMuseum))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Invalid museum");

            verifyNoInteractions(harvardApiClient, clevelandApiClient);
        }

        @Test
        @DisplayName("Should fetch artwork from The Cleveland Museum of Art")
        void fetchFromClevelandMuseum_Success() {
            Artwork expectedArtwork = Artwork.builder()
                    .museumName("The Cleveland Museum of Art")
                    .sourceArtworkId(123)
                    .title("The Starry Night")
                    .build();

            when(clevelandApiClient.fetchArtworkDetail(123)).thenReturn(expectedArtwork);

            Artwork actualArtwork = exhibitionServiceImp.fetchFromApi(123, "The Cleveland Museum of Art");

            assertThat(actualArtwork).isNotNull();
            assertThat(actualArtwork.getTitle()).isEqualTo("The Starry Night");

            verify(clevelandApiClient, times(1)).fetchArtworkDetail(123);
            verifyNoInteractions(harvardApiClient);
        }

    }
}
