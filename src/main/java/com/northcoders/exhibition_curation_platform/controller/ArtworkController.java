package com.northcoders.exhibition_curation_platform.controller;

import com.northcoders.exhibition_curation_platform.model.Artwork;
import com.northcoders.exhibition_curation_platform.service.ArtworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ArtworkController {

    @Autowired
    ArtworkService artworkService;

    @GetMapping("/search-results/{museum}")
    public ResponseEntity<Map<String, Object>> getArtworks(
            @PathVariable String museum,
            @RequestParam (required = false) String keyword,
            @RequestParam (required = false) String artist,
            @RequestParam (defaultValue = "1") int page
    ){
        Map<String, String> museumMap = Map.of(
                "harvard", "Harvard Art Museum",
                "cleveland", "The Cleveland Museum of Art"
        );

        String fullMuseumName = museumMap.get(museum.toLowerCase());

        if (fullMuseumName == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid museum name"));
        }

        List<Artwork> artworks = artworkService.getArtworks(keyword, artist, fullMuseumName, page);
        boolean hasNextPage = artworks.size() > 10;
        if (hasNextPage) {
            artworks = artworks.subList(0, 10);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("artworks", artworks);
        response.put("currentPage", page);
        response.put("prevPage", page>1 ? page-1 : null);
        response.put("nextPage", hasNextPage ? page+1 : null);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{museum}/artwork/{sourceId}")
    public ResponseEntity<Artwork> getArtworkDetail(@PathVariable int sourceId){
        return null;
    }


}
