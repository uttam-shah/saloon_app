package com.example.saloon.app.saloon_app.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.saloon.app.saloon_app.dto.saloonShop.RegisterShopDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.RegisterShopPatchDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.response.SalonShopResponseDto;
import com.example.saloon.app.saloon_app.entity.SalonShop;
import com.example.saloon.app.saloon_app.entity.Users;
import com.example.saloon.app.saloon_app.repository.AuthRepository;
import com.example.saloon.app.saloon_app.repository.SalonShopRepository;
import com.example.saloon.app.saloon_app.service.SalonShopService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SalonShopServiceImpl implements SalonShopService {

    private final SalonShopRepository salonShopRepository;
    private final AuthRepository authRepository;
    private final ModelMapper modelMapper;
    @Autowired
    private Cloudinary cloudinary;

    public SalonShopResponseDto RegisterShop(RegisterShopDto registerShopDto) {
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

        return modelMapper.map(salonShop, SalonShopResponseDto.class);
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

    @Override
    public SalonShopResponseDto updateShop(String shopId, RegisterShopDto dto) {
        SalonShop shop = salonShopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop no flund"));

        shop.setShopName(dto.getShopName());
        shop.setActive(dto.isActive());
        shop.setShopDescription(dto.getShopDescription());
        shop.setPhone(dto.getPhone());
        shop.setCoverImage(dto.getCoverImage());
        shop.setPhotos(dto.getPhotos());
        shop.setServiceCount(dto.getServiceCount());
        shop.setAppointmentsToday(dto.getAppointmentsToday());
        shop.setTodayEarnings(dto.getTodayEarnings());
        shop.setAddress1(dto.getAddress1());
        shop.setAddress2(dto.getAddress2());
        shop.setCity(dto.getCity());
        shop.setState(dto.getState());
        shop.setPostalCode(dto.getPostalCode());
        shop.setCountry(dto.getCountry());

        SalonShop updated = salonShopRepository.save(shop);

        return  modelMapper.map(updated, SalonShopResponseDto.class);
    }


    @Override
    public SalonShopResponseDto patchShop(String shopId, RegisterShopPatchDto dto) {

        SalonShop shop = salonShopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        if (dto.getOwnerId() != null) {
            Users owner = authRepository.findById(dto.getOwnerId())
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
            shop.setOwner(owner);
        }

        if (dto.getShopName() != null) shop.setShopName(dto.getShopName());
        if (dto.getShopDescription() != null) shop.setShopDescription(dto.getShopDescription());
        if (dto.getPhone() != null) shop.setPhone(dto.getPhone());
        if (dto.getIsActive() != null) shop.setActive(dto.getIsActive());
        if (dto.getServiceCount() != null) shop.setServiceCount(dto.getServiceCount());
        if (dto.getAppointmentsToday() != null) shop.setAppointmentsToday(dto.getAppointmentsToday());
        if (dto.getTodayEarnings() != null) shop.setTodayEarnings(dto.getTodayEarnings());

        if (dto.getAddress1() != null) shop.setAddress1(dto.getAddress1());
        if (dto.getAddress2() != null) shop.setAddress2(dto.getAddress2());
        if (dto.getCity() != null) shop.setCity(dto.getCity());
        if (dto.getState() != null) shop.setState(dto.getState());
        if (dto.getPostalCode() != null) shop.setPostalCode(dto.getPostalCode());
        if (dto.getCountry() != null) shop.setCountry(dto.getCountry());

        if (dto.getLatitude() != null) shop.setLatitude(dto.getLatitude());
        if (dto.getLongitude() != null) shop.setLongitude(dto.getLongitude());

        // Save updated shop
        SalonShop updated = salonShopRepository.save(shop);

        return modelMapper.map(updated, SalonShopResponseDto.class);
    }

    @Override
    public String uploadCoverImage(String shopId, MultipartFile file) {
        try {
            return uploadImage(file);
        }
        catch (IOException e){
            throw new RuntimeException("Exception: ", e);
        }

    }

    @Override
    public List<String> uploadShopPhotos(String shopId, List<MultipartFile> files) {
        return List.of();
    }

    String uploadImage(MultipartFile file) throws IOException {

        Map uploadResult = cloudinary.uploader()
                .upload(file.getBytes(), ObjectUtils.emptyMap());

        String fileUrl = uploadResult.get("secure_url").toString();

        return fileUrl;
    }

}
