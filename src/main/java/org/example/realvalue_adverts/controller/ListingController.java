package org.example.realvalue_adverts.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.example.realvalue_adverts.service.ListingService;
import org.example.realvalue_adverts.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Validated
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/listings")
public class ListingController {

    @Autowired
    private ListingService listingService;

    @Autowired
    private SearchService searchService;

    @GetMapping("/top/{page}")
    public ResponseEntity<Map<String, Object>> getTopListings(
            @PathVariable @Min(1) int page) {
        Map<String, Object> listingsMap = listingService.getTopListings(9, page);
        return ResponseEntity.ok(listingsMap);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchListings(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "9") @Min(1) @Max(100) int limit) {

        Map<String, Object> searchResults = searchService.searchListings(query, limit, page);
        return ResponseEntity.ok(searchResults);

    }

    @GetMapping("/total-pages")
    public ResponseEntity<Map<String, Integer>> getTotalPages() {
        int totalPages = listingService.getTotalPages();
        return ResponseEntity.ok(Map.of("totalPages", totalPages));
    }

    @GetMapping("/card_id/{card_id}")
    public ResponseEntity<Map<String, Object>> getListingById(
            @PathVariable("card_id") String cardId) {
        Map<String, Object> listing = listingService.getListingById(cardId);
        if (listing.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(listing);
        }
        return ResponseEntity.ok(listing);
    }

    @GetMapping("/group/{card_id}")
    public ResponseEntity<Map<String, Object>> getGroupedListings(
            @PathVariable("card_id") String cardId) {
        Map<String, Object> groupedListings = listingService.getGroupedListings(cardId);
        if (groupedListings.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(groupedListings);
        }
        return ResponseEntity.ok(groupedListings);
    }
}