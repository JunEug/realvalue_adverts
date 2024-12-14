package org.example.realvalue_adverts.controller;

import org.example.realvalue_adverts.service.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/listings")
public class ListingController {

    @Autowired
    private ListingService listingService;

    @GetMapping("/top/{page}")
    public ResponseEntity<Map<String, Object>> getTopListings(@PathVariable int page) {
        Map<String, Object> listingsMap = listingService.getTopListings(9, page);

        return ResponseEntity.ok(listingsMap);
    }
}
