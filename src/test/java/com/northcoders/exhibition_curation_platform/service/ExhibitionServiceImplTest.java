package com.northcoders.exhibition_curation_platform.service;

import com.northcoders.exhibition_curation_platform.exception.ItemNotFoundException;
import com.northcoders.exhibition_curation_platform.model.Artwork;
import com.northcoders.exhibition_curation_platform.model.Exhibition;
import com.northcoders.exhibition_curation_platform.repository.ArtworkRepository;
import com.northcoders.exhibition_curation_platform.repository.ExhibitionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExhibitionServiceImplTest {

    @Mock
    private HarvardApiClient harvardApiClient;

    @Mock
    private ClevelandApiClient clevelandApiClient;

    @Mock
    private ExhibitionRepository mockExhibitionRepository;

    @Mock
    private ArtworkRepository mockArtworkRepository;

    @InjectMocks
    private ExhibitionServiceImp exhibitionServiceImp;


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
            when(mockExhibitionRepository.save(any(Exhibition.class))).thenReturn(exhibition);
            Exhibition actual = exhibitionServiceImp.createExhibition("Monet");
            assertThat(actual.getName()).isEqualTo("Monet");
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
            assertThat(actual.getName()).isEqualTo("new");

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
            String sourceId = "789";
            String invalidMuseum = "Unknown";

            assertThatThrownBy(() -> exhibitionServiceImp.fetchFromApi(sourceId, invalidMuseum))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Invalid museum");

            verifyNoInteractions(harvardApiClient, clevelandApiClient);
        }

        @Test
        @DisplayName("Should fetch artwork from The Cleveland Museum of Art")
        void fetchFromClevelandMuseum() {
            Artwork expectedArtwork = Artwork.builder()
                    .museumName("The Cleveland Museum of Art")
                    .sourceArtworkId("123")
                    .title("The Starry Night")
                    .build();

            when(clevelandApiClient.fetchArtworkDetail("123")).thenReturn(expectedArtwork);

            Artwork actualArtwork = exhibitionServiceImp.fetchFromApi("123", "The Cleveland Museum of Art");

            assertThat(actualArtwork).isNotNull();
            assertThat(actualArtwork.getTitle()).isEqualTo("The Starry Night");

            verify(clevelandApiClient, times(1)).fetchArtworkDetail("123");
            verifyNoInteractions(harvardApiClient);
        }

        @Test
        @DisplayName("Should fetch artwork from The Harvard Museum of Art")
        void fetchFromHarvardMuseum() {
            Artwork expectedArtwork = Artwork.builder()
                    .museumName("Harvard Art Museum")
                    .sourceArtworkId("123")
                    .title("The Starry Night")
                    .build();

            when(harvardApiClient.fetchArtworkDetail("123")).thenReturn(expectedArtwork);

            Artwork actualArtwork = exhibitionServiceImp.fetchFromApi("123", "Harvard Art Museum");

            assertThat(actualArtwork).isNotNull();
            assertThat(actualArtwork.getTitle()).isEqualTo("The Starry Night");

            verify(harvardApiClient, times(1)).fetchArtworkDetail("123");
            verifyNoInteractions(clevelandApiClient);
        }

    }

    @Nested
    class AddArtworkToExhibition {
        @Test
        @DisplayName("Should throw exception when exhibition id is invalid")
        void invalidExhibitionId () throws Exception {
            when (mockExhibitionRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(ItemNotFoundException.class, () -> exhibitionServiceImp.addArtworkToExhibition(1L,"100", "Harvard Art Museum"));
        }

        @Test
        @DisplayName("Should not add existing Artwork when artwork is in Database")
        void existingArtwork() {
            String sourceId = "100";
            String museum = "The Cleveland Museum of Art";
            Artwork existingArtwork = Artwork.builder()
                    .sourceArtworkId(sourceId)
                    .museumName(museum)
                    .exhibitions(new HashSet<>())
                    .id(1L)
                    .build();

            Long exhibitionId = 1L;
            Set<Artwork> artworks = new HashSet<>();
            artworks.add(existingArtwork);
            Exhibition exhibition = Exhibition.builder()
                    .id(exhibitionId)
                    .artworks(artworks)
                    .build();

            when(mockExhibitionRepository.findById(exhibitionId)).thenReturn(Optional.of(exhibition));
            when(mockArtworkRepository.findBySourceArtworkIdAndMuseumName(sourceId, museum))
                    .thenReturn(Optional.of(existingArtwork));
            when(mockExhibitionRepository.save(exhibition)).thenReturn(exhibition);

            Exhibition result = exhibitionServiceImp.addArtworkToExhibition(exhibitionId, sourceId, museum);

            assertEquals(1, result.getArtworks().size());
            verify(mockArtworkRepository, never()).save(any());
            verify(mockExhibitionRepository).save(exhibition);
        }

        @Test
        @DisplayName("Should add new artwork when artwork is not in database")
        void newArtwork() {

            String sourceId = "100";
            String museum = "The Cleveland Museum of Art";

            Artwork newArtwork = Artwork.builder()
                    .sourceArtworkId(sourceId)
                    .museumName(museum)
                    .exhibitions(new HashSet<>())
                    .build();

            when(clevelandApiClient.fetchArtworkDetail(sourceId)).thenReturn(newArtwork);

            Long exhibitionId = 1L;
            Exhibition exhibition = Exhibition.builder()
                    .id(exhibitionId)
                    .artworks(new HashSet<>())
                    .build();

            when(mockExhibitionRepository.findById(exhibitionId)).thenReturn(Optional.of(exhibition));
            when(mockArtworkRepository.findBySourceArtworkIdAndMuseumName(sourceId, museum))
                    .thenReturn(Optional.empty());
            when(mockArtworkRepository.save(any(Artwork.class)))
                    .thenReturn(newArtwork);
            when(mockExhibitionRepository.save(exhibition)).thenReturn(exhibition);

            Exhibition result = exhibitionServiceImp.addArtworkToExhibition(exhibitionId, sourceId, museum);

            verify(mockArtworkRepository).save(any(Artwork.class));
            verify(mockExhibitionRepository).save(exhibition);

            assertTrue(result.getArtworks().contains(newArtwork));
        }



    }

    @Nested
    class RemoveArtworkFromExhibition {
        @Test
        @DisplayName("Successfully remove artwork from exhibition")
        void removeArtworkFromExhibition_Success() {
            Long exhibitionId = 1L;
            Long artworkId = 2L;

            Exhibition exhibition = Exhibition.builder()
                    .id(exhibitionId)
                    .artworks(new HashSet<>())
                    .build();

            Artwork artwork = Artwork.builder()
                    .id(artworkId)
                    .exhibitions(new HashSet<>())
                    .build();

            exhibition.getArtworks().add(artwork);
            artwork.getExhibitions().add(exhibition);

            when(mockExhibitionRepository.findById(exhibitionId)).thenReturn(Optional.of(exhibition));
            when(mockArtworkRepository.findById(artworkId)).thenReturn(Optional.of(artwork));
            when(mockExhibitionRepository.save(exhibition)).thenReturn(exhibition);

            // Act
            Exhibition result = exhibitionServiceImp.removeArtworkFromExhibition(exhibitionId, artworkId);

            // Assert
            assertFalse(result.getArtworks().contains(artwork));
            assertFalse(artwork.getExhibitions().contains(exhibition));
            verify(mockExhibitionRepository).save(exhibition);
        }

        @Test
        @DisplayName("Throw exception when artwork not found")
        void removeArtworkFromExhibition_ArtworkNotFound() {
            // Arrange
            Long exhibitionId = 1L;
            Long artworkId = 2L;

            Exhibition exhibition = new Exhibition();
            when(mockExhibitionRepository.findById(exhibitionId)).thenReturn(Optional.of(exhibition));
            when(mockArtworkRepository.findById(artworkId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ItemNotFoundException.class, () ->
                    exhibitionServiceImp.removeArtworkFromExhibition(exhibitionId, artworkId)
            );
            verify(mockExhibitionRepository, never()).save(any());
        }

        @Test
        @DisplayName("Throw exception when exhibition not found")
        void removeArtworkFromExhibition_ExhibitionNotFound() {
            // Arrange
            Long exhibitionId = 1L;
            Long artworkId = 2L;

            when(mockExhibitionRepository.findById(exhibitionId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ItemNotFoundException.class, () ->
                    exhibitionServiceImp.removeArtworkFromExhibition(exhibitionId, artworkId)
            );
            verify(mockArtworkRepository, never()).findById(any());
            verify(mockExhibitionRepository, never()).save(any());
        }


    }

}
