package com.example.saloon.app.saloon_app.controller;

import com.example.saloon.app.saloon_app.dto.demo.DemoShopDto;
import com.example.saloon.app.saloon_app.service.demo.DemoDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/demo-data")
public class DemoDataController {

    private final DemoDataService demoDataService;

    // Seeds (or re-seeds, replacing prior demo data) 5 demo shops around the given location
    @PostMapping("/seed")
    public ResponseEntity<List<DemoShopDto>> seedDemoShops(
            @RequestParam double latitude,
            @RequestParam double longitude) {

        return ResponseEntity.ok(demoDataService.seedDemoShops(latitude, longitude));
    }

    // Lists previously seeded demo shops, sorted by distance from the given location
    @GetMapping("/shops")
    public ResponseEntity<List<DemoShopDto>> getDemoShops(
            @RequestParam double latitude,
            @RequestParam double longitude) {

        return ResponseEntity.ok(demoDataService.getDemoShops(latitude, longitude));
    }
}
