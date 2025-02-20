package com.northcoders.exhibition_curation_platform.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import jakarta.persistence.*;

import java.util.List;


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

    @ManyToMany
    private List<Gallery> galleries;



}
