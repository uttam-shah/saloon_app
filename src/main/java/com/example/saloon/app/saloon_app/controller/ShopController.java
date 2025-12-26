package com.example.saloon.app.saloon_app.controller;

import com.cloudinary.Cloudinary;
import com.example.saloon.app.saloon_app.dto.saloonShop.RegisterShopDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.RegisterShopPatchDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.response.SalonShopResponseDto;
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
            @RequestBody @Valid RegisterShopDto registerShopDto,
            @RequestParam("coverImage") MultipartFile coverImage,
            @RequestParam("photos") List<MultipartFile> photos
    ){
        System.out.println("POST shop");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(salonShopService.RegisterShop(registerShopDto, coverImage, photos));
    }

    // Get Shops by UserId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SalonShopResponseDto>> getShops(@PathVariable String userId){
        System.out.println("GET shop/user/"+userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(salonShopService.getShops(userId));
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

    //Patch for partial update
    @PatchMapping(value = "/{shopId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SalonShopResponseDto> patchShop(
            @PathVariable String shopId,
            @ModelAttribute RegisterShopPatchDto dto,
            @RequestParam("coverImage") MultipartFile coverImage,
            @RequestParam("photos") List<MultipartFile> photos
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(salonShopService.patchShop(shopId, dto, coverImage, photos ));
    }

    @PostMapping("/{shopId}/cover-image")
    public ResponseEntity<String> uploadCoverImage(
            @RequestParam("coverImage") MultipartFile file) {

        String url = salonShopService.uploadCoverImage(file);
        return ResponseEntity.ok(url);
    }

    @PostMapping("/{shopId}/photos")
    public ResponseEntity<List<String>> uploadShopPhotos(
            @RequestParam("photos") List<MultipartFile> files) {

        List<String> urls = salonShopService.uploadShopPhotos(files);
        return ResponseEntity.ok(urls);
    }



}
