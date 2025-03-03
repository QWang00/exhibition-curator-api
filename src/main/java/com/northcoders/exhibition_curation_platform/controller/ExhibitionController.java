package com.northcoders.exhibition_curation_platform.controller;

import com.northcoders.exhibition_curation_platform.model.Exhibition;
import com.northcoders.exhibition_curation_platform.service.ExhibitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ExhibitionController {

    @Autowired
    ExhibitionService exhibitionService;

    @GetMapping("/exhibitions")
    public ResponseEntity<List<Exhibition>> getAllExhibitions() {
        return new ResponseEntity<List<Exhibition>>(exhibitionService.getAllExhibitions(), HttpStatus.OK);
    }

    @GetMapping("/exhibition/{id}")
    public ResponseEntity<Exhibition> getExhibitionById(@PathVariable Long id) {
        return new ResponseEntity<Exhibition>(exhibitionService.getExhibitionById(id), HttpStatus.OK);
    }

    @PostMapping("/exhibitions")
    public ResponseEntity<Exhibition> createExhibition(@RequestParam String name) {
        return new ResponseEntity<Exhibition>(exhibitionService.createExhibition(name), HttpStatus.CREATED);
    }

    @PutMapping("/exhibition/{id}/name")
    public ResponseEntity<Exhibition> updateExhibitionName(
            @PathVariable Long id,
            @RequestParam String newName
    ) {
        return new ResponseEntity<Exhibition>(exhibitionService.updateExhibitionNameById(id, newName), HttpStatus.OK);
    }

    @PostMapping("exhibition/{id}/artworks")
    public ResponseEntity<Exhibition> addArtwork(
            @PathVariable Long id,
            @RequestParam String sourceArtworkId,
            @RequestParam String museum
    ) {
        return new ResponseEntity<Exhibition> (exhibitionService.addArtworkToExhibition(id, sourceArtworkId, museum), HttpStatus.OK);
    }

    @DeleteMapping("/exhibition/{exhibitionId}/artworks/{artworkId}")
    public ResponseEntity<Void> removeArtwork(
            @PathVariable Long exhibitionId,
            @PathVariable Long artworkId
    ) {
        exhibitionService.removeArtworkFromExhibition(exhibitionId, artworkId);
        return ResponseEntity.noContent().build();
    }
}