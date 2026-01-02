package com.example.saloon.app.saloon_app.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.saloon.app.saloon_app.dto.saloonShop.RegisterShopDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.RegisterShopPatchDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.response.SalonShopResponseDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.response.ShopDetailDto;
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
        System.out.println(registerShopDto);
//        modelMapper.typeMap(RegisterShopDto.class, SalonShop.class)
//                .addMappings(mapper -> mapper.skip(SalonShop::setOpeningHours));
        SalonShop newShop = modelMapper.map(registerShopDto, SalonShop.class);
        System.out.println(newShop);


        newShop.setShopId(null);
        newShop.setCreatedAt(null);
        newShop.setUpdatedAt(null);

        Users user = authRepository.findById(registerShopDto.getOwnerId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        newShop.setOwner(user);
        // Save shop first to get shopId
        SalonShop savedShop = salonShopRepository.save(newShop);

//       savedShop.setOpeningHours(openingHours);
        return modelMapper.map(savedShop, SalonShopResponseDto.class);
    }

    @Override
    public SalonShopResponseDto patchShop(
            String shopId,
            RegisterShopPatchDto dto,
            @Nullable MultipartFile coverImage,
            @Nullable List<MultipartFile> photos
    ) {
        SalonShop shop = salonShopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        // Update owner if provided
        if (dto.getOwnerId() != null) {
            Users owner = authRepository.findById(dto.getOwnerId())
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
            shop.setOwner(owner);
        }

        // Update cover image if provided
        if (coverImage != null && !coverImage.isEmpty()) {
            try {
                String coverUrl = uploadImage(coverImage);
                shop.setCoverImage(coverUrl);
            } catch (Exception e) {
                System.out.println("Error while uploading cover image: " + e);
            }
        }

        // Add new photos if provided (append to existing)
        if (photos != null && !photos.isEmpty()) {
            // Get current max sequence
            int currentMaxSequence = shop.getPhotos().stream()
                    .filter(p -> !p.isDeleted())
                    .mapToInt(ShopImages::getSequence)
                    .max()
                    .orElse(0);

            int remainingSlots = 10 - (int) shop.getPhotos().stream()
                    .filter(p -> !p.isDeleted())
                    .count();

            int photosToAdd = Math.min(photos.size(), remainingSlots);

            for (int i = 0; i < photosToAdd; i++) {
                MultipartFile file = photos.get(i);
                try {
                    String photoUrl = uploadImage(file);

                    ShopImages shopImage = ShopImages.builder()
                            .shop(shop)
                            .sequence(currentMaxSequence + i + 1)
                            .imageUrl(photoUrl)
                            .isDeleted(false)
                            .build();

                    shop.addPhoto(shopImage);
                } catch (Exception e) {
                    System.out.println("Error while uploading photo: " + e);
                }
            }
        }

        // Update other fields
        if (dto.getShopName() != null) shop.setShopName(dto.getShopName());
        if (dto.getShopDescription() != null) shop.setShopDescription(dto.getShopDescription());
        if (dto.getPhone() != null) shop.setPhone(dto.getPhone());
        if (dto.getIsActive() != null) shop.setActive(dto.getIsActive());
        // ... update other fields ...

        SalonShop updated = salonShopRepository.save(shop);
        return modelMapper.map(updated, SalonShopResponseDto.class);
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
    public String uploadCoverImage(MultipartFile file) {

        try {
            return uploadImage(file);
        }
        catch (IOException e){
            throw new RuntimeException("Exception: ", e);
        }

    }

    @Override
    public List<String> uploadShopPhotos(List<MultipartFile> files) {
        List<String> links = new LinkedList<>();

        for(MultipartFile file : files){
            try {
                links.add(uploadImage(file));
            }
            catch (Exception e){
                System.out.println(e);
            }

        }

        return  links;

    }

    String uploadImage(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader()
                .upload(file.getBytes(), ObjectUtils.emptyMap());

        return uploadResult.get("secure_url").toString();
    }

}
