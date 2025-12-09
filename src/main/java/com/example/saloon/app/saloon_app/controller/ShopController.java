package com.example.saloon.app.saloon_app.controller;

import com.example.saloon.app.saloon_app.dto.saloonShop.RegisterShopDto;
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
@RequestMapping("shop")
public class ShopController {

    private  final SalonShopService salonShopService;

    @PostMapping("/register")
    private ResponseEntity<RegisterShopDto> registerShop(@RequestBody @Valid RegisterShopDto registerShopDto){
        System.out.println("shop/register");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(salonShopService.RegisterShop(registerShopDto));
    }

    @GetMapping("/getShops/{userId}")
    private ResponseEntity<List<SalonShopResponseDto>> getShops(@PathVariable String userId){
        System.out.println("shop/getShops/"+userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(salonShopService.getShops(userId));
    }

}
