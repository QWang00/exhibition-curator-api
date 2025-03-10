package com.northcoders.exhibition_curation_platform.config;

import com.northcoders.exhibition_curation_platform.model.Artwork;
import com.northcoders.exhibition_curation_platform.model.Exhibition;
import java.util.HashSet;
import java.util.Set;

public class SeedData {

    public static Set<Artwork> createArtworks() {
        Set<Artwork> artworks = new HashSet<>();
        artworks.add(Artwork.builder()
                .sourceArtworkId("1958.39")
                .imageUrl("https://openaccess-cdn.clevelandart.org/1958.39/1958.39_print.jpg")
                .museumName("The Cleveland Museum of Art")
                .title("The Red Kerchief")
                .yearMade("500 BC")
                .artist("Unknown")
                .culture("Greek")
                .artistActiveYear("Ancient Period")
                .description("An ancient Greek vase used for storing wine.")
                .tombstone("Ancient Greek Vase, 500 BC, Ceramic")
                .preview("Preview of an ancient Greek vase.")
                .build());

        artworks.add(Artwork.builder()
                .sourceArtworkId("231940")
                .imageUrl("https://nrs.harvard.edu/urn-3:HUAM:797168")
                .museumName("Harvard Art Museum")
                .title("Mona Lisa")
                .yearMade("1503")
                .artist("Leonardo da Vinci")
                .culture("Italian")
                .artistActiveYear("Renaissance")
                .description("A portrait of Lisa Gherardini, wife of Francesco del Giocondo.")
                .tombstone("Mona Lisa by Leonardo da Vinci, 1503, Oil on wood")
                .preview("Preview of the Mona Lisa.")
                .build());

        return artworks;
    }

    // In SeedData.java's createExhibitions method:

    public static Set<Exhibition> createExhibitions(Set<Artwork> artworks) {
        Set<Exhibition> exhibitions = new HashSet<>();
        Exhibition exhibition = Exhibition.builder()
                .name("Renaissance Art")
                .build();

        // Use the addArtwork method to manage both sides of the relationship
        for (Artwork artwork : artworks) {
            exhibition.addArtwork(artwork); // <-- This ensures bidirectional sync
        }

        exhibitions.add(exhibition);
        return exhibitions;
    }

}

