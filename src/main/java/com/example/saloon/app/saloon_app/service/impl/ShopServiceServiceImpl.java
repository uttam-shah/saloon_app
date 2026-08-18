package com.example.saloon.app.saloon_app.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.saloon.app.saloon_app.dto.ShopService.ShopServiceDto;
import com.example.saloon.app.saloon_app.dto.ShopService.ShopServiceImageDto;
import com.example.saloon.app.saloon_app.dto.ShopService.ShopServicePatchDto;
import com.example.saloon.app.saloon_app.dto.ShopService.ShopServiceResponseDto;
import com.example.saloon.app.saloon_app.config.security.CurrentUser;
import com.example.saloon.app.saloon_app.dto.saloonShop.ShopDistanceProjection;
import com.example.saloon.app.saloon_app.entity.SalonShop;
import com.example.saloon.app.saloon_app.entity.ShopService;
import com.example.saloon.app.saloon_app.entity.ShopServiceImage;
import com.example.saloon.app.saloon_app.entity.Users;
import com.example.saloon.app.saloon_app.exception.ForbiddenException;
import com.example.saloon.app.saloon_app.repository.AuthRepository;
import com.example.saloon.app.saloon_app.repository.SalonShopRepository;
import com.example.saloon.app.saloon_app.repository.ShopServiceImageRepository;
import com.example.saloon.app.saloon_app.repository.ShopServiceRepository;
import com.example.saloon.app.saloon_app.service.ShopServiceService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopServiceServiceImpl implements ShopServiceService {

    private final ShopServiceRepository shopServiceRepository;
    private final ShopServiceImageRepository shopServiceImageRepository;
    private final SalonShopRepository salonShopRepository;
    private final  ModelMapper modelMapper;
    private  final AuthRepository authRepository;
    private final Cloudinary cloudinary;

    @Override
    public ShopServiceResponseDto registerService(ShopServiceDto shopServiceDto) {

        ShopService newShopService = modelMapper.map(shopServiceDto, ShopService.class);

        newShopService.setServiceId(null);

        SalonShop salonShop = salonShopRepository.findById(shopServiceDto.getShopId())
                .orElseThrow(() -> new RuntimeException("ShopId not found"));

        requireOwnership(salonShop);

        newShopService.setShop(salonShop);

        ShopService saved = shopServiceRepository.save(newShopService);

        return toResponseDto(saved);
    }

    @Override
    public List<ShopServiceResponseDto> getServicesByUserId(String userId) {
        Users users = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));
        List<ShopService> services = shopServiceRepository.findByShop_Owner_UserId(userId);

        return services.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShopServiceResponseDto> getServicesByShopId(String shopId) {
        SalonShop salonShop = salonShopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop Id not found"));

        List<ShopService> services = shopServiceRepository.findByShop_ShopId(shopId);

        return services.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ShopServiceResponseDto updateService(String serviceId, ShopServiceDto dto) {
        ShopService service = shopServiceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        requireOwnership(service.getShop());

        // Replace all fields
        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setDurationMinutes(dto.getDurationMinutes());
        service.setPrice(dto.getPrice());
        service.setIsActive(dto.getIsActive());
        service.setIsSmartQueueEnabled(dto.getIsSmartQueueEnabled() != null ? dto.getIsSmartQueueEnabled() : false);

        // Update shop if required
        SalonShop shop = salonShopRepository.findById(dto.getShopId())
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        service.setShop(shop);

        ShopService saved = shopServiceRepository.save(service);
        return toResponseDto(saved);

    }

    @Override
    public ShopServiceResponseDto patchService(String serviceId, ShopServicePatchDto dto) {
        ShopService service = shopServiceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        requireOwnership(service.getShop());

        if (dto.getName() != null) service.setName(dto.getName());
        if (dto.getDescription() != null) service.setDescription(dto.getDescription());
        if (dto.getDurationMinutes() != null) service.setDurationMinutes(dto.getDurationMinutes());
        if (dto.getPrice() != null) service.setPrice(dto.getPrice());
        if (dto.getIsActive() != null) service.setIsActive(dto.getIsActive());
        if (dto.getIsSmartQueueEnabled() != null) service.setIsSmartQueueEnabled(dto.getIsSmartQueueEnabled());

        ShopService saved = shopServiceRepository.save(service);
        return toResponseDto(saved);
    }

    @Override
    public List<ShopServiceResponseDto> getTrendingServices(double latitude, double longitude, Double radiusKm, Integer limit) {
        double radius = (radiusKm != null && radiusKm > 0) ? radiusKm : 1.0;
        int maxResults = (limit != null && limit > 0) ? limit : 20;

        List<ShopDistanceProjection> nearbyShops = salonShopRepository.findNearbyShops(
                latitude, longitude, radius * 1000, maxResults
        );

        List<String> shopIds = nearbyShops.stream()
                .map(ShopDistanceProjection::getShopId)
                .collect(Collectors.toList());

        if (shopIds.isEmpty()) {
            return List.of();
        }

        List<ShopService> services = shopServiceRepository.findByShop_ShopIdInAndIsActiveTrue(shopIds);

        return services.stream()
                .limit(maxResults)
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShopServiceImageDto> uploadServiceImages(List<MultipartFile> files, String serviceId) {
        ShopService service = shopServiceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        requireOwnership(service.getShop());

        List<ShopServiceImageDto> uploaded = new LinkedList<>();
        int nextSequence = service.getImages().size();

        for (MultipartFile file : files) {
            try {
                String url = uploadImage(file);
                ShopServiceImage image = ShopServiceImage.builder()
                        .service(service)
                        .imageUrl(url)
                        .sequence(nextSequence++)
                        .isDeleted(false)
                        .build();
                ShopServiceImage saved = shopServiceImageRepository.save(image);
                uploaded.add(modelMapper.map(saved, ShopServiceImageDto.class));
            } catch (IOException e) {
                throw new RuntimeException("Exception while uploading service image: ", e);
            }
        }

        return uploaded;
    }

    @Override
    public void deleteServiceImage(String serviceId, String imageId) {
        ShopService service = shopServiceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        requireOwnership(service.getShop());

        ShopServiceImage image = shopServiceImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        image.setDeleted(true);
        shopServiceImageRepository.save(image);
    }

    private ShopServiceResponseDto toResponseDto(ShopService service) {
        ShopServiceResponseDto dto = modelMapper.map(service, ShopServiceResponseDto.class);

        List<ShopServiceImageDto> images = service.getImages().stream()
                .filter(img -> !img.isDeleted())
                .map(img -> modelMapper.map(img, ShopServiceImageDto.class))
                .collect(Collectors.toList());

        dto.setImages(images);
        dto.setCoverImage(images.isEmpty() ? null : images.get(0).getImageUrl());

        return dto;
    }

    private String uploadImage(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader()
                .upload(file.getBytes(), ObjectUtils.emptyMap());

        return uploadResult.get("secure_url").toString();
    }

    private void requireOwnership(SalonShop shop) {
        if (shop.getOwner() == null || !shop.getOwner().getUserId().equals(CurrentUser.id())) {
            throw new ForbiddenException("You do not own this shop");
        }
    }

}
