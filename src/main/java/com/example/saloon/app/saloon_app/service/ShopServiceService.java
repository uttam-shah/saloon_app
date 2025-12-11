package com.example.saloon.app.saloon_app.service;

import com.example.saloon.app.saloon_app.dto.ShopService.ShopServiceDto;
import com.example.saloon.app.saloon_app.dto.ShopService.ShopServiceResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface ShopServiceService {
    ShopServiceResponseDto registerService(@Valid ShopServiceDto shopServiceDto);

    List<ShopServiceResponseDto> getServicesByUserId(String userId);

    List<ShopServiceResponseDto> getServicesByShopId(String shopId);
}
