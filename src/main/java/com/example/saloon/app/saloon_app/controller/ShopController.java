package com.example.saloon.app.saloon_app.controller;

import com.example.saloon.app.saloon_app.dto.saloonShop.RegisterShopDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.RegisterShopPatchDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.response.SalonShopResponseDto;
import com.example.saloon.app.saloon_app.entity.SalonShop;
import com.example.saloon.app.saloon_app.service.SalonShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/shop")
public class ShopController {

    private  final SalonShopService salonShopService;

    // Register Shop
    @PostMapping()
    public ResponseEntity<SalonShopResponseDto> registerShop(@RequestBody @Valid RegisterShopDto registerShopDto){
        System.out.println("POST shop");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(salonShopService.RegisterShop(registerShopDto));
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
    @PatchMapping("/{shopId}")
    public ResponseEntity<SalonShopResponseDto> patchShop(
            @PathVariable String shopId,
            @RequestBody RegisterShopPatchDto dto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(salonShopService.patchShop(shopId, dto));
    }

}
