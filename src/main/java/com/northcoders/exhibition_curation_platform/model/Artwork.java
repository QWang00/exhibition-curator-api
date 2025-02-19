package com.northcoders.exhibition_curation_platform.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity

public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private String artistPlace;
    private String artistActiveYear;

    @Lob
    private String tombstone;

    @Lob
    private String preview;

    @ManyToMany
    private List<Gallery> galleries;



}
