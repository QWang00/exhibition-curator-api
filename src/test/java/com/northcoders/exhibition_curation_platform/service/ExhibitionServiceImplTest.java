package com.northcoders.exhibition_curation_platform.service;

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
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
    }
}
