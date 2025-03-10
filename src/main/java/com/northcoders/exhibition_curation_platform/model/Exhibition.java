package com.northcoders.exhibition_curation_platform.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Exhibition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JsonManagedReference
    @JoinTable(
            name = "exhibition_artwork",
            joinColumns = @JoinColumn(name = "exhibition_id"),
            inverseJoinColumns = @JoinColumn(name = "artwork_id")
    )
    @Builder.Default // <-- Ensures the field is initialized when using @Builder
    private Set<Artwork> artworks = new HashSet<>();


    public void addArtwork(Artwork artwork) {
        artworks.add(artwork);
        artwork.getExhibitions().add(this);
    }

    public void removeArtwork(Artwork artwork) {
        artworks.remove(artwork);
        artwork.getExhibitions().remove(this);
    }
}
