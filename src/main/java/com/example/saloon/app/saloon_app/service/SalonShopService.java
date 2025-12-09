package com.example.saloon.app.saloon_app.service;

import com.example.saloon.app.saloon_app.dto.saloonShop.RegisterShopDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.response.SalonShopResponseDto;
import com.example.saloon.app.saloon_app.entity.SalonShop;

import java.util.List;

public interface SalonShopService {

    RegisterShopDto RegisterShop(RegisterShopDto registerShopDto);

    List<SalonShopResponseDto> getShops(String userId);
}
