package com.example.saloon.app.saloon_app.service;

import com.example.saloon.app.saloon_app.dto.ShopService.ShopServiceDto;
import com.example.saloon.app.saloon_app.dto.ShopService.ShopServiceResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface ShopServiceService {
    ShopServiceResponseDto registerService(@Valid ShopServiceDto shopServiceDto);

    List<ShopServiceResponseDto> getShopsByUserId(String userId);
}
