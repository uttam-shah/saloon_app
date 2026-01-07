package com.example.saloon.app.saloon_app.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.saloon.app.saloon_app.dto.saloonShop.RegisterShopDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.RegisterShopPatchDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.response.OpeningHourDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.response.SalonShopResponseDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.response.ShopDetailDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.response.ShopImageDto;
import com.example.saloon.app.saloon_app.entity.SalonShop;
import com.example.saloon.app.saloon_app.entity.ShopImages;
import com.example.saloon.app.saloon_app.entity.ShopOpeningHours;
import com.example.saloon.app.saloon_app.entity.Users;
import com.example.saloon.app.saloon_app.repository.AuthRepository;
import com.example.saloon.app.saloon_app.repository.SalonShopRepository;
import com.example.saloon.app.saloon_app.repository.ShopImagesRepository;
import com.example.saloon.app.saloon_app.repository.ShopOpeningHoursRepository;
import com.example.saloon.app.saloon_app.service.SalonShopService;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SalonShopServiceImpl implements SalonShopService {

    private final SalonShopRepository salonShopRepository;
    private final ShopImagesRepository shopImagesRepository;
    private final AuthRepository authRepository;
    private final ModelMapper modelMapper;
    private  final ShopOpeningHoursRepository openingHoursRepository;

    @Autowired
    private Cloudinary cloudinary;

    public SalonShopResponseDto RegisterShop(
            RegisterShopDto registerShopDto
    ) {
        System.out.println("RegisterShop Method");
        System.out.println("Body data: "+registerShopDto);
//        modelMapper.typeMap(RegisterShopDto.class, SalonShop.class)
//                .addMappings(mapper -> mapper.skip(SalonShop::setOpeningHours));
//        SalonShop newShop = modelMapper.map(registerShopDto, SalonShop.class);
        SalonShop newShop = SalonShop.builder()
                .shopName(registerShopDto.getShopName())
                .shopDescription(registerShopDto.getShopDescription())
                .phone(registerShopDto.getPhone())
                .isActive(true)
                .serviceCount(0)
                .appointmentsToday(0)
                .todayEarnings(0)
//                .coverImage(registerShopDto.getCover())
                .address1(registerShopDto.getAddress1())
                .address2(registerShopDto.getAddress2())
                .city(registerShopDto.getCity())
                .state(registerShopDto.getState())
                .postalCode(registerShopDto.getPostalCode())
                .country(registerShopDto.getCountry())
                .latitude(registerShopDto.getLatitude())
                .longitude(registerShopDto.getLongitude())
                .build();
        System.out.println("Mapped Shop: " + newShop);

        newShop.setShopId(null);
        newShop.setCreatedAt(null);
        newShop.setUpdatedAt(null);

        Users user = authRepository.findById(registerShopDto.getOwnerId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        newShop.setOwner(user);
        // Save shop first to get shopId
        SalonShop savedShop = salonShopRepository.save(newShop);

        if(registerShopDto.getOpeningHours() != null && !registerShopDto.getOpeningHours().isEmpty()) {
            List<ShopOpeningHours> hoursList = new LinkedList<>();

            for (OpeningHourDto oh : registerShopDto.getOpeningHours()) {
                ShopOpeningHours hours = ShopOpeningHours.builder()
                        .shop(savedShop)
                        .dayOfWeek(oh.getDayOfWeek())
                        .isOpen(oh.isOpen())
                        .openTime(oh.getOpenTime())
                        .closeTime(oh.getCloseTime())
                        .build();
                hoursList.add(hours);
            }
            savedShop.getOpeningHours().addAll(hoursList);
        }

        SalonShop finalShop = salonShopRepository.save(savedShop);
//       savedShop.setOpeningHours(openingHours);
        return modelMapper.map(finalShop, SalonShopResponseDto.class);
    }

    @Override
    @Transactional
    public SalonShopResponseDto patchShop(
            String shopId,
            RegisterShopPatchDto dto
    ) {
        // Find existing shop
        SalonShop shop = salonShopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found with ID: " + shopId));

        // Update owner if provided
        if (dto.getOwnerId() != null) {
            Users owner = authRepository.findById(dto.getOwnerId())
                    .orElseThrow(() -> new RuntimeException("Owner not found with ID: " + dto.getOwnerId()));
            shop.setOwner(owner);
        }

        // Update basic fields
        if (dto.getShopName() != null) {
            shop.setShopName(dto.getShopName());
        }
        if (dto.getShopDescription() != null) {
            shop.setShopDescription(dto.getShopDescription());
        }
        if (dto.getPhone() != null) {
            shop.setPhone(dto.getPhone());
        }
        if (dto.getIsActive() != null) {
            shop.setActive(dto.getIsActive());
        }

        // Update address fields
        if (dto.getAddress1() != null) {
            shop.setAddress1(dto.getAddress1());
        }
        if (dto.getAddress2() != null) {
            shop.setAddress2(dto.getAddress2());
        }
        if (dto.getCity() != null) {
            shop.setCity(dto.getCity());
        }
        if (dto.getState() != null) {
            shop.setState(dto.getState());
        }
        if (dto.getPostalCode() != null) {
            shop.setPostalCode(dto.getPostalCode());
        }
        if (dto.getCountry() != null) {
            shop.setCountry(dto.getCountry());
        }

        // Update location coordinates
        if (dto.getLatitude() != null) {
            shop.setLatitude(dto.getLatitude());
        }
        if (dto.getLongitude() != null) {
            shop.setLongitude(dto.getLongitude());
        }

        // Update opening hours if provided
        if (dto.getOpeningHours() != null) {
            updateOpeningHours(shop, dto.getOpeningHours());
        }

        // Save and return
        SalonShop updatedShop = salonShopRepository.save(shop);
        return modelMapper.map(updatedShop, SalonShopResponseDto.class);
    }

    private void updateOpeningHours(SalonShop shop, List<OpeningHourDto> newHours) {
        // Clear existing hours (triggers orphan removal for deleted items)
        shop.getOpeningHours().clear();

        // Add new hours to the SAME collection (don't replace the collection!)
        if (newHours != null && !newHours.isEmpty()) {
            for (OpeningHourDto dto : newHours) {
                ShopOpeningHours hours = ShopOpeningHours.builder()
                        .shop(shop)
                        .dayOfWeek(dto.getDayOfWeek())
                        .isOpen(dto.isOpen())
                        .openTime(dto.getOpenTime())
                        .closeTime(dto.getCloseTime())
                        .build();

                // Add to existing collection (NOT shop.setOpeningHours())
                shop.getOpeningHours().add(hours);
            }
        }
    }

    @Override
    public void deletePhoto(String shopId, String imageId) {
        SalonShop shop = salonShopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        ShopImages imageToDelete = shop.getPhotos().stream()
                .filter(img -> img.getImageId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // Soft delete
        imageToDelete.setDeleted(true);
        shopImagesRepository.save(imageToDelete);

        // OR Hard delete (remove from database)
        // shop.removePhoto(imageToDelete);
        // shopImagesRepository.delete(imageToDelete);
        // salonShopRepository.save(shop);
    }

    @Override
    public void reorderPhotos(String shopId, List<String> imageIdsInOrder) {
        SalonShop shop = salonShopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        for (int i = 0; i < imageIdsInOrder.size(); i++) {
            String imageId = imageIdsInOrder.get(i);
            ShopImages image = shop.getPhotos().stream()
                    .filter(img -> img.getImageId().equals(imageId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Image not found: " + imageId));

            image.setSequence(i + 1);
        }

        salonShopRepository.save(shop);
    }

    @Override
    public ShopDetailDto getShopById(String shopId) {
        SalonShop shop = salonShopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("shopId not found"));

        return modelMapper.map(shop, ShopDetailDto.class);
    }

    @Override
    public List<SalonShopResponseDto> getShops(String userId) {
        Users owner = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not flund"));
        List<SalonShop> salonShops = salonShopRepository.getByOwner(owner);

        List<SalonShopResponseDto> salonShopResponseDto =  new LinkedList<>();

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
//        shop.setCoverImage(dto.getCoverImage());
//        shop.setPhotos(dto.getPhotos());
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
    public String uploadCoverImage(MultipartFile file, String shopId) {

        try {
            SalonShop shop = salonShopRepository.findById(shopId)
                    .orElseThrow(() -> new RuntimeException("userId not found"));
            var url = uploadImage(file);
            shop.setCoverImage(url);
            salonShopRepository.save(shop);
            return  url;
        }
        catch (IOException e){
            throw new RuntimeException("Exception: ", e);
        }

    }

    @Override
    public List<ShopImageDto> uploadShopPhotos(List<MultipartFile> files, String shopId) {
        List<String> links = new LinkedList<>();
        List<ShopImageDto> linksDto = new LinkedList<>();

        for(MultipartFile file : files){
            try {
                links.add(uploadImage(file));
            }
            catch (Exception e){
                System.out.println(e);
            }

        }

        SalonShop shop = salonShopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("ShopId not found"));

        for(String link: links){
            ShopImages image = ShopImages.builder()
                    .shop(shop)
                    .imageUrl(link)
                    .isDeleted(false)
                    .build();

            ShopImages I = shopImagesRepository.save(image);
            linksDto.add(modelMapper.map(I, ShopImageDto.class));
        }

        return  linksDto;

    }

    String uploadImage(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader()
                .upload(file.getBytes(), ObjectUtils.emptyMap());

        return uploadResult.get("secure_url").toString();
    }

}
