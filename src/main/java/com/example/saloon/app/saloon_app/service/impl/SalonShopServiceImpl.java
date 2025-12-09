package com.example.saloon.app.saloon_app.service.impl;

import com.example.saloon.app.saloon_app.dto.saloonShop.RegisterShopDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.response.SalonShopResponseDto;
import com.example.saloon.app.saloon_app.entity.SalonShop;
import com.example.saloon.app.saloon_app.entity.Users;
import com.example.saloon.app.saloon_app.repository.AuthRepository;
import com.example.saloon.app.saloon_app.repository.SalonShopRepository;
import com.example.saloon.app.saloon_app.service.SalonShopService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SalonShopServiceImpl implements SalonShopService {

    private final SalonShopRepository salonShopRepository;
    private final AuthRepository authRepository;
    private final ModelMapper modelMapper;

//    @Override
//    public RegisterShopDto RegisterShop(RegisterShopDto registerShopDto) {
//        SalonShop newShop = modelMapper.map(registerShopDto, SalonShop.class);
//
//        Users user = authRepository.findById(registerShopDto.getOwnerId()).orElseThrow(() -> new RuntimeException("User not found"));
//        newShop.setOwner(user);
//        SalonShop salonShop = salonShopRepository.save(newShop);
//
//        return  modelMapper.map(salonShop, RegisterShopDto.class);
//    }

    public RegisterShopDto RegisterShop(RegisterShopDto registerShopDto) {
        SalonShop newShop = modelMapper.map(registerShopDto, SalonShop.class);

        // Force shopId to null to ensure INSERT operation
        newShop.setShopId(null);

        // Also ensure timestamps are not set
        newShop.setCreatedAt(null);
        newShop.setUpdatedAt(null);

        Users user = authRepository.findById(registerShopDto.getOwnerId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        newShop.setOwner(user);

        SalonShop salonShop = salonShopRepository.save(newShop);

        return modelMapper.map(salonShop, RegisterShopDto.class);
    }

    @Override
    public List<SalonShopResponseDto> getShops(String userId) {
        Users owner = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not flund"));
        List<SalonShop> salonShops = salonShopRepository.getByOwner(owner);

        List<SalonShopResponseDto> salonShopResponseDto =  new ArrayList<>();

        for (int i=0; i < salonShops.size(); i++){
            salonShopResponseDto.add(modelMapper.map(salonShops.get(i), SalonShopResponseDto.class));
        }

        return  salonShopResponseDto;
    }
}
