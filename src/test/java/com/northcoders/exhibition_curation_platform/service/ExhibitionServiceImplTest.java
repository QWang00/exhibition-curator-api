package com.northcoders.exhibition_curation_platform.service;

import com.northcoders.exhibition_curation_platform.exception.ItemNotFoundException;
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
    }
}
