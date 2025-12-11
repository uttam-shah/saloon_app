package com.example.saloon.app.saloon_app.service.impl;

import com.example.saloon.app.saloon_app.dto.ShopService.ShopServiceDto;
import com.example.saloon.app.saloon_app.dto.ShopService.ShopServiceResponseDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.response.SalonShopResponseDto;
import com.example.saloon.app.saloon_app.entity.SalonShop;
import com.example.saloon.app.saloon_app.entity.ShopService;
import com.example.saloon.app.saloon_app.entity.Users;
import com.example.saloon.app.saloon_app.repository.AuthRepository;
import com.example.saloon.app.saloon_app.repository.SalonShopRepository;
import com.example.saloon.app.saloon_app.repository.ShopServiceRepository;
import com.example.saloon.app.saloon_app.service.ShopServiceService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopServiceServiceImpl implements ShopServiceService {

    private final ShopServiceRepository shopServiceRepository;
    private final SalonShopRepository salonShopRepository;
    private final  ModelMapper modelMapper;
    private  final AuthRepository authRepository;

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
//        response.setShop(modelMapper.map(salonShop, SalonShopResponseDto.class));

        return response;
    }

    @Override
    public List<ShopServiceResponseDto> getShopsByUserId(String userId) {
        Users users = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));
        List<ShopService> services = shopServiceRepository.findByShop_Owner_UserId(userId);

//        return Collections.singletonList(modelMapper.map(salonShops, ShopServiceResponseDto.class));
        // Convert List<ShopService> → List<ShopServiceResponseDto>
        return services.stream()
                .map(service -> modelMapper.map(service, ShopServiceResponseDto.class))
                .collect(Collectors.toList());
    }

}
