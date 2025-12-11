package com.example.saloon.app.saloon_app.controller;


import com.example.saloon.app.saloon_app.dto.ShopService.ShopServiceDto;
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
@RequestMapping("shop_service")
public class ShopServiceController {
    private final ShopServiceService shopServiceService;

    @PostMapping("/register")
    private ResponseEntity<ShopServiceResponseDto> registerService(@RequestBody @Valid ShopServiceDto shopServiceDto){
        System.out.println("shop_service/register");
        return  ResponseEntity
                .status(HttpStatus.OK)
                .body(shopServiceService.registerService(shopServiceDto));
    }

    @GetMapping("/get_services_by_user_id/{userId}")
    private ResponseEntity<List<ShopServiceResponseDto>>getServices(@PathVariable String userId ){
        System.out.println("shop_services/get_services_by_user_id/"+userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(shopServiceService.getShopsByUserId(userId));
    }
}
