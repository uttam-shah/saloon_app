package com.example.saloon.app.saloon_app.controller;


import com.example.saloon.app.saloon_app.dto.ShopService.ShopServiceDto;
import com.example.saloon.app.saloon_app.dto.ShopService.ShopServicePatchDto;
import com.example.saloon.app.saloon_app.dto.ShopService.ShopServiceResponseDto;
import com.example.saloon.app.saloon_app.service.ShopServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/shop-services")
public class ShopServiceController {
    private final ShopServiceService shopServiceService;

    //Register Service
    @PostMapping
    public ResponseEntity<ShopServiceResponseDto> registerService(
            @RequestBody @Valid ShopServiceDto shopServiceDto){
        System.out.println("POST /shop-services");
        return  ResponseEntity
                .status(HttpStatus.OK)
                .body(shopServiceService.registerService(shopServiceDto));
    }

    // GET SERVICES BY USER ID (RESTFUL)
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<ShopServiceResponseDto>> getServicesByUserId(
            @PathVariable String userId ){
        System.out.println("GET /shop-services/users/" + userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(shopServiceService.getServicesByUserId(userId));
    }

    // GET SERVICES BY SHOP ID
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<ShopServiceResponseDto>> getServicesByShopId(
            @PathVariable String shopId){
        System.out.println("GET /shop-services/shop/" + shopId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(shopServiceService.getServicesByShopId(shopId));
    }

    // GET Trending Services
    @GetMapping("/shop/trending")
    public ResponseEntity<List<ShopServiceResponseDto>> getTrendingServices(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(required = false, defaultValue = "1.0") Double radiusKm,
            @RequestParam(required = false, defaultValue = "20") Integer limit){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(shopServiceService.getTrendingServices(
                        latitude, longitude, radiusKm, limit
                ));
    }

    // put Mapping for full update
    @PutMapping("/{serviceId}")
    public ResponseEntity<ShopServiceResponseDto> updateService(
            @PathVariable String serviceId,
            @Valid @RequestBody ShopServiceDto shopServiceDto) {

        return ResponseEntity.ok(
                shopServiceService.updateService(serviceId, shopServiceDto)
        );
    }

    // Patch Mapping for partial update
    @PatchMapping("/{serviceId}")
    public ResponseEntity<ShopServiceResponseDto> patchService(
            @PathVariable String serviceId,
            @RequestBody ShopServicePatchDto patchDto) {

        return ResponseEntity.ok(
                shopServiceService.patchService(serviceId, patchDto)
        );
    }



}
