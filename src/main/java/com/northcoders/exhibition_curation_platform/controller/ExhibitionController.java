package com.northcoders.exhibition_curation_platform.controller;

import com.northcoders.exhibition_curation_platform.model.Exhibition;
import com.northcoders.exhibition_curation_platform.service.ExhibitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CacheConfig(cacheNames = "exhibitionsCache")
@Tag(name = "Exhibition", description = "Exhibition management APIs with caching")
public class ExhibitionController {

    @Autowired
    ExhibitionService exhibitionService;

    @Operation(summary = "Get all exhibitions", description = "Retrieve a list of all exhibitions (cached)")
    @GetMapping("/exhibitions")
    @Cacheable
    public ResponseEntity<List<Exhibition>> getAllExhibitions() {
        return new ResponseEntity<List<Exhibition>>(exhibitionService.getAllExhibitions(), HttpStatus.OK);
    }

    @Operation(summary = "Get exhibition by ID", description = "Retrieve a exhibition by its ID (cached)")
    @GetMapping("/exhibition/{id}")
    @Cacheable(key = "#id")
    public ResponseEntity<Exhibition> getExhibitionById(@PathVariable Long id) {
        return new ResponseEntity<Exhibition>(exhibitionService.getExhibitionById(id), HttpStatus.OK);
    }

    @Operation(summary = "Create a new exhibition", description = "Create a new exhibition with the provided details")
    @PostMapping("/exhibitions")
    @CacheEvict(value = "exhibitionsCache", allEntries = true)
    public ResponseEntity<Exhibition> createExhibition(@RequestParam String name) {
        return new ResponseEntity<Exhibition>(exhibitionService.createExhibition(name), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an exhibition", description = "Update an existing exhibition by its ID")
    @PutMapping("/exhibition/{id}/name")
    @CacheEvict(value = "exhibitionsCache", allEntries = true)
    public ResponseEntity<Exhibition> updateExhibitionName(
            @PathVariable Long id,
            @RequestParam String newName
    ) {
        return new ResponseEntity<Exhibition>(exhibitionService.updateExhibitionNameById(id, newName), HttpStatus.OK);
    }

    @Operation(summary = "Add an artwork into an exhibition", description = "Add an artwork into an exhibition with the provided details. " +
            "Note: The 'museum' parameter must be either 'cleveland' or 'harvard'.")
    @PostMapping("exhibition/{id}/artworks")
    @CacheEvict(value = "exhibitionsCache", allEntries = true)
    public ResponseEntity<Exhibition> addArtwork(
            @PathVariable Long id,
            @RequestParam String sourceArtworkId,
            @RequestParam String museum
    ) {
        Map<String, String> museumMap = Map.of(
                "harvard", "Harvard Art Museum",
                "cleveland", "The Cleveland Museum of Art"
        );

        String fullMuseumName = museumMap.get(museum.toLowerCase());
        if(fullMuseumName == null) {
            return ResponseEntity.badRequest().body(null);
        }

        return new ResponseEntity<Exhibition> (exhibitionService.addArtworkToExhibition(id, sourceArtworkId, fullMuseumName), HttpStatus.OK);
    }

    @Operation(summary = "Remove an artwork from an exhibition", description = "remove an artwork from an exhibition")
    @DeleteMapping("/exhibition/{exhibitionId}/artworks/{artworkId}")
    @CacheEvict(value = "exhibitionsCache", allEntries = true)
    public ResponseEntity<Void> removeArtwork(
            @PathVariable Long exhibitionId,
            @PathVariable Long artworkId
    ) {
        exhibitionService.removeArtworkFromExhibition(exhibitionId, artworkId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add an artwork from local database into an exhibition", description = "Add an artwork from local database into an exhibition using its artworkId")
    @PostMapping("exhibition/{id}/artworks/local")
    @CacheEvict(value = "exhibitionsCache", allEntries = true)
    public ResponseEntity<Exhibition> addArtworkFromLocal(
            @PathVariable Long id,
            @RequestParam Long artworkId
    ) {
        return new ResponseEntity<Exhibition>(exhibitionService.addArtworkToExhibitionFromLocal(id, artworkId), HttpStatus.OK);
    }

}