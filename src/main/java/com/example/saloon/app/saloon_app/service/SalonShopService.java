package com.example.saloon.app.saloon_app.service;

import com.example.saloon.app.saloon_app.dto.saloonShop.RegisterShopDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.RegisterShopPatchDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.response.SalonShopResponseDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.response.ShopDetailDto;
import com.example.saloon.app.saloon_app.entity.SalonShop;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SalonShopService {

    SalonShopResponseDto RegisterShop(RegisterShopDto registerShopDto, MultipartFile coverImage, List<MultipartFile> photos);

    List<SalonShopResponseDto> getShops(String userId);

    SalonShopResponseDto updateShop(String shopId, RegisterShopDto registerShopDto);

    SalonShopResponseDto patchShop(String shopId, RegisterShopPatchDto dto, MultipartFile coverImage, List<MultipartFile> photos);

    String uploadCoverImage(MultipartFile file);

    List<String> uploadShopPhotos(List<MultipartFile> files);

    void deletePhoto(String shopId, String imageId);

    void reorderPhotos(String shopId, List<String> imageIdsInOrder);

    ShopDetailDto getShopById(String shopId);
}
