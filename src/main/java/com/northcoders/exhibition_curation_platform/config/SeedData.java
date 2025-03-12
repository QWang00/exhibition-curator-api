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
                .title("The Red Kerchief")
                .yearMade("500 BC")
                .artist("Unknown")
                .culture("Greek")
                .artistActiveYear("Ancient Period")
                .tombstone("Ancient Greek Vase, 500 BC, Ceramic")
                .imageUrl("https://openaccess-cdn.clevelandart.org/1958.39/1958.39_print.jpg")
                .museumName("cleveland")
                .description("This painting depicts Monet's first wife, Camille, outside on a snowy day passing by the French doors of their home at Argenteuil. Her face is rendered in a radically bold Impressionist technique of mere daubs of paint quickly applied, just as the snow and trees are defined by broad, broken strokes of pure white and green.")
                .preview("The Red Kerchief, c. 1868–73. Claude Monet (French, 1840–1926)")
                .build());

        artworks.add(Artwork.builder()
                .sourceArtworkId("231940")
                .imageUrl("https://nrs.harvard.edu/urn-3:HUAM:797168")
                .museumName("harvard")
                .title("Mona Lisa")
                .yearMade("1503")
                .artist("Leonardo da Vinci")
                .culture("Italian")
                .artistActiveYear("Renaissance")
                .description("We look down at two fish that have been laid on a crumpled white cloth on a pale yellow table or counter top. The fish lie diagonally, side by side, one with its head closer to us and its tail pointed up and to the right, and the other facing opposite, with its tail close by the head of the other fish and its head pointing away. They both have  shimmering coral pink and white skin and small orange tails and fins.")
                .preview("Red Mullets, c. 1870. Claude Monet (French, 1840 - 1926).")
                .build());

        return artworks;
    }



    public static Set<Exhibition> createExhibitions(Set<Artwork> artworks) {
        Set<Exhibition> exhibitions = new HashSet<>();
        Exhibition exhibition = Exhibition.builder()
                .name("Renaissance Art")
                .build();


        for (Artwork artwork : artworks) {
            exhibition.addArtwork(artwork);
        }

        exhibitions.add(exhibition);
        return exhibitions;
    }

}

