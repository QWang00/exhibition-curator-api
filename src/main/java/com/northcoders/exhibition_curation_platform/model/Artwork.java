package com.northcoders.exhibition_curation_platform.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Data
@Builder
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private int sourceArtworkId;

    @NotNull
    @Column(nullable = false)
    private String imageUrl;

    @NotNull
    @Column(nullable = false)
    private String museumName;

    private String title;
    private String yearMade;
    private String artist;
    private String culture;
    private String artistActiveYear;
    private String description;

    @Lob
    private String tombstone;

    @Lob
    private String preview;

    @ManyToMany(mappedBy = "artworks")
    private Set<Exhibition> exhibitions = new HashSet<>();

}
