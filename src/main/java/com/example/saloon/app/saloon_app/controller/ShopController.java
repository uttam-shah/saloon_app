package com.example.saloon.app.saloon_app.controller;

import com.example.saloon.app.saloon_app.dto.registerShop.RegisterShopDto;
import com.example.saloon.app.saloon_app.service.SalonShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

}
