package com.northcoders.exhibition_curation_platform.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Builder
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String sourceArtworkId;

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

    @Column(columnDefinition = "TEXT")
    private String description;

    @Lob
    private String tombstone;

    @Lob
    private String preview;

    @Builder.Default
    @ManyToMany(mappedBy = "artworks")
    @JsonBackReference
    private Set<Exhibition> exhibitions = new HashSet<>();

}
