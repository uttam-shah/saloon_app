package com.example.saloon.app.saloon_app.service.impl;

import com.example.saloon.app.saloon_app.dto.ShopService.ShopServiceDto;
import com.example.saloon.app.saloon_app.dto.ShopService.ShopServicePatchDto;
import com.example.saloon.app.saloon_app.dto.ShopService.ShopServiceResponseDto;
import com.example.saloon.app.saloon_app.config.security.CurrentUser;
import com.example.saloon.app.saloon_app.dto.saloonShop.response.SalonShopResponseDto;
import com.example.saloon.app.saloon_app.entity.SalonShop;
import com.example.saloon.app.saloon_app.entity.ShopService;
import com.example.saloon.app.saloon_app.entity.Users;
import com.example.saloon.app.saloon_app.exception.ForbiddenException;
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

        requireOwnership(salonShop);

        newShopService.setShop(salonShop);

        ShopService saved = shopServiceRepository.save(newShopService);

        ShopServiceResponseDto response = modelMapper.map(saved, ShopServiceResponseDto.class);

        // manually set shopId instead of entity
//        response.setShop(modelMapper.map(salonShop, SalonShopResponseDto.class));

        return response;
    }

    @Override
    public List<ShopServiceResponseDto> getServicesByUserId(String userId) {
        Users users = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));
        List<ShopService> services = shopServiceRepository.findByShop_Owner_UserId(userId);

        // Convert List<ShopService> → List<ShopServiceResponseDto>
        return services.stream()
                .map(service -> modelMapper.map(service, ShopServiceResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ShopServiceResponseDto> getServicesByShopId(String shopId) {
        SalonShop salonShop = salonShopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop Id not found"));

        List<ShopService> services = shopServiceRepository.findByShop_ShopId(shopId);

        return services.stream()
                .map(service -> modelMapper.map(service, ShopServiceResponseDto.class))
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
        return modelMapper.map(saved, ShopServiceResponseDto.class);

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
        return modelMapper.map(saved, ShopServiceResponseDto.class);
    }

    private void requireOwnership(SalonShop shop) {
        if (shop.getOwner() == null || !shop.getOwner().getUserId().equals(CurrentUser.id())) {
            throw new ForbiddenException("You do not own this shop");
        }
    }

}
