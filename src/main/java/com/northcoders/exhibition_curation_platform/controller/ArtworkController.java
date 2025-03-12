package com.northcoders.exhibition_curation_platform.controller;

import com.northcoders.exhibition_curation_platform.model.Artwork;
import com.northcoders.exhibition_curation_platform.service.ArtworkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CacheConfig(cacheNames = "artworksCache")
@Tag(name = "Artwork", description = "Artwork management APIs with caching")
public class ArtworkController {

    @Autowired
    ArtworkService artworkService;

    @Operation(summary = "Get all artworks by keyword, artist, and museum", description = "Retrieve a list of artworks based on keywords (cached)" +
            "Note: The 'museum' parameter must be either 'cleveland' or 'harvard'.")
    @GetMapping("/search-results/{museum}")
    @Cacheable
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

    @Operation(summary = "Get artwork details using the artwork ID from its source museum", description = "Retrieve an artwork by its ID (cached)" +
            "Note: The 'museum' parameter must be either 'cleveland' or 'harvard'.")
    @GetMapping("/{museum}/artwork/{sourceId}")
    @Cacheable(key = "#sourceId")
    public ResponseEntity<Artwork> getArtworkDetails(
            @PathVariable String sourceId,
            @PathVariable String museum
    ){
        Map<String, String> museumMap = Map.of(
                "harvard", "Harvard Art Museum",
                "cleveland", "The Cleveland Museum of Art"
        );

        String fullMuseumName = museumMap.get(museum.toLowerCase());

        if (fullMuseumName == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try{
            Artwork artwork = artworkService.getArtworkDetails(sourceId, fullMuseumName);
            return ResponseEntity.ok(artwork);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }


}
