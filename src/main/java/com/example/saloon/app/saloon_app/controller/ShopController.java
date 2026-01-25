package com.example.saloon.app.saloon_app.controller;

import com.cloudinary.Cloudinary;
import com.example.saloon.app.saloon_app.dto.saloonShop.NearbyShopDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.RegisterShopDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.RegisterShopPatchDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.response.SalonShopResponseDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.response.ShopDetailDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.response.ShopImageDto;
import com.example.saloon.app.saloon_app.entity.SalonShop;
import com.example.saloon.app.saloon_app.service.SalonShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/shop")
public class ShopController {

    private  final SalonShopService salonShopService;
    @Autowired
    private Cloudinary cloudinary;

    // Register Shop
    @PostMapping()
    public ResponseEntity<SalonShopResponseDto> registerShop(
            @RequestBody @Valid RegisterShopDto registerShopDto
//            @RequestParam(value = "coverImage", required = false) MultipartFile coverImage,
//            @RequestParam(value = "photos", required = false) List<MultipartFile> photos
    ){
        System.out.println("POST shop");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(salonShopService.RegisterShop(registerShopDto));
    }


    //Patch for partial update
    @PatchMapping(value = "/{shopId}")
    public ResponseEntity<SalonShopResponseDto> patchShop(
            @PathVariable String shopId,
            @RequestBody RegisterShopPatchDto dto
//            @RequestParam(value = "coverImage", required = false) MultipartFile coverImage,
//            @RequestParam(value = "photos", required = false) List<MultipartFile> photos
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(salonShopService.patchShop(shopId, dto ));
    }

    @PostMapping("/{shopId}/cover-image")
    public ResponseEntity<String> uploadCoverImage(
            @PathVariable String shopId,
            @RequestParam("coverImage") MultipartFile file) {

        String url = salonShopService.uploadCoverImage(file, shopId);
        return ResponseEntity.ok(url);
    }

    @PostMapping("/{shopId}/photos")
    public ResponseEntity<List<ShopImageDto>> uploadShopPhotos(
            @PathVariable String shopId,
            @RequestParam("photos") List<MultipartFile> files) {

        System.out.println("/shop/"+shopId+"/photos"+files);
        List<ShopImageDto> urls = salonShopService.uploadShopPhotos(files, shopId);
        return ResponseEntity.ok(urls);
    }

    // Get Shops by UserId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SalonShopResponseDto>> getShops(
            @PathVariable String userId){
        System.out.println("GET shop/user/"+userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(salonShopService.getShops(userId));
    }

    // Get Shop by shopId
    @GetMapping("/{shopId}")
    public ResponseEntity<ShopDetailDto> getShopById(
            @PathVariable String shopId){
        return  ResponseEntity
                .status(HttpStatus.OK)
                .body(salonShopService.getShopById(shopId));
    }

    // put Mapping for full shop update
    @PutMapping("/{shopId}")
    public ResponseEntity<SalonShopResponseDto> updateShop(
            @PathVariable String shopId,
            @Valid @RequestBody RegisterShopDto registerShopDto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(salonShopService.updateShop(shopId, registerShopDto));
    }

    // Delete photo endpoint
    @DeleteMapping("/{shopId}/photos/{imageId}")
    public ResponseEntity<Void> deletePhoto(
            @PathVariable String shopId,
            @PathVariable String imageId
    ) {
        salonShopService.deletePhoto(shopId, imageId);
        return ResponseEntity.noContent().build();
    }

    // Reorder photos endpoint
    @PutMapping("/{shopId}/photos/reorder")
    public ResponseEntity<Void> reorderPhotos(
            @PathVariable String shopId,
            @RequestBody List<String> imageIdsInOrder
    ) {
        salonShopService.reorderPhotos(shopId, imageIdsInOrder);
        return ResponseEntity.ok().build();
    }

    /**
     * GET /api/shops/nearby?latitude=28.6139&longitude=77.2090&radiusKm=1&limit=20
     */
    @GetMapping("/nearby")
    public ResponseEntity<List<NearbyShopDto>> getNearbyShops(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(required = false, defaultValue = "1.0") Double radiusKm,
            @RequestParam(required = false, defaultValue = "20") Integer limit
    ) {
        List<NearbyShopDto> nearbyShops = salonShopService.findNearbySalons(
                latitude, longitude, radiusKm, limit
        );

        return ResponseEntity.ok(nearbyShops);
    }

    @DeleteMapping("/{shopId}")
        public ResponseEntity<Void>  deleteShop(
                @PathVariable String shopId
        ){
        salonShopService.deleteShop(shopId);
        return ResponseEntity.noContent().build();
    }



}
