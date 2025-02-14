package com.northcoders.exhibition_curation_platform.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String description;

    @NotNull
    @Column(nullable = false)
    private String imageUrl;

    @NotNull
    @Column(nullable = false)
    private String museumName;

    private String title;
    private String yearMade;
    private String artist;
    private String artistActiveYear;

    @Lob
    private String tombstone;

    @Lob
    private String preview;

    @ManyToMany
    private List<Gallery> galleries;



}
