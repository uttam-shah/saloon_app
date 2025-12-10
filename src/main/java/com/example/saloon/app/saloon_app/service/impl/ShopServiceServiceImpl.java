package com.example.saloon.app.saloon_app.service.impl;

import com.example.saloon.app.saloon_app.dto.ShopService.ShopServiceDto;
import com.example.saloon.app.saloon_app.dto.ShopService.ShopServiceResponseDto;
import com.example.saloon.app.saloon_app.entity.SalonShop;
import com.example.saloon.app.saloon_app.entity.ShopService;
import com.example.saloon.app.saloon_app.repository.SalonShopRepository;
import com.example.saloon.app.saloon_app.repository.ShopServiceRepository;
import com.example.saloon.app.saloon_app.service.ShopServiceService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopServiceServiceImpl implements ShopServiceService {

    private final ShopServiceRepository shopServiceRepository;
    private final SalonShopRepository salonShopRepository;
    private final  ModelMapper modelMapper;

    @Override
    public ShopServiceResponseDto registerService(ShopServiceDto shopServiceDto) {

        ShopService newShopService = modelMapper.map(shopServiceDto, ShopService.class);

        newShopService.setServiceId(null);

        SalonShop salonShop = salonShopRepository.findById(shopServiceDto.getShopId())
                .orElseThrow(() -> new RuntimeException("ShopId not found"));

        newShopService.setShop(salonShop);

        ShopService saved = shopServiceRepository.save(newShopService);

        ShopServiceResponseDto response = modelMapper.map(saved, ShopServiceResponseDto.class);

        // manually set shopId instead of entity
        response.setShopId(salonShop.getShopId());

        return response;
    }

}
