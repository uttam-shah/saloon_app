package com.example.saloon.app.saloon_app.service;

import com.example.saloon.app.saloon_app.dto.saloonShop.RegisterShopDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.RegisterShopPatchDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.response.SalonShopResponseDto;
import com.example.saloon.app.saloon_app.entity.SalonShop;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SalonShopService {

    SalonShopResponseDto RegisterShop(RegisterShopDto registerShopDto);

    List<SalonShopResponseDto> getShops(String userId);

    SalonShopResponseDto updateShop(String shopId, RegisterShopDto registerShopDto);

    SalonShopResponseDto patchShop(String shopId, RegisterShopPatchDto dto);

    String uploadCoverImage(String shopId, MultipartFile file);

    List<String> uploadShopPhotos(String shopId, List<MultipartFile> files);
}
