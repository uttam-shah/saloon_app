package com.example.saloon.app.saloon_app.service.demo;

import com.example.saloon.app.saloon_app.dto.demo.DemoShopDto;
import com.example.saloon.app.saloon_app.dto.demo.DemoShopServiceDto;
import com.example.saloon.app.saloon_app.entity.SalonShop;
import com.example.saloon.app.saloon_app.entity.ShopService;
import com.example.saloon.app.saloon_app.entity.ShopServiceImage;
import com.example.saloon.app.saloon_app.entity.Users;
import com.example.saloon.app.saloon_app.repository.AuthRepository;
import com.example.saloon.app.saloon_app.repository.SalonShopRepository;
import com.example.saloon.app.saloon_app.repository.ShopServiceImageRepository;
import com.example.saloon.app.saloon_app.repository.ShopServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Seeds a fixed set of demo salon shops/services around a given location so the
 * app can be demoed without needing real shop-owner sign-ups. Only runs when
 * explicitly triggered from the "Demo Data" settings screen — never on startup.
 */
@Service
@RequiredArgsConstructor
public class DemoDataService {

    private static final String DEMO_OWNER_EMAIL = "demo.owner@saloonapp.local";

    private final AuthRepository authRepository;
    private final SalonShopRepository salonShopRepository;
    private final ShopServiceRepository shopServiceRepository;
    private final ShopServiceImageRepository shopServiceImageRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String[] SERVICE_IMAGE_POOL = {
            "https://images.unsplash.com/photo-1503951914875-452162b0f3f1?w=800&q=80",
            "https://images.unsplash.com/photo-1562322140-8baeececf3df?w=800&q=80",
            "https://images.unsplash.com/photo-1519415387722-a1c3bbef716c?w=800&q=80",
            "https://images.unsplash.com/photo-1516975080664-ed2fc6a32937?w=800&q=80",
            "https://images.unsplash.com/photo-1470259078422-826894b933aa?w=800&q=80",
    };

    private static final DemoShopTemplate[] TEMPLATES = {
            new DemoShopTemplate(
                    "The Grooming Lounge", "Premium unisex salon offering haircuts, styling and grooming.",
                    "12 MG Road", "Bengaluru", "Karnataka", "560001",
                    "https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?w=800&q=80",
                    0.012, 0.008
            ),
            new DemoShopTemplate(
                    "Urban Cuts Barbershop", "Classic barbershop with modern grooming services.",
                    "45 Church Street", "Bengaluru", "Karnataka", "560001",
                    "https://images.unsplash.com/photo-1521590832167-7bcbfaa6381f?w=800&q=80",
                    -0.010, 0.015
            ),
            new DemoShopTemplate(
                    "Glow Beauty Studio", "Full-service beauty studio for hair, skin and makeup.",
                    "78 Brigade Road", "Bengaluru", "Karnataka", "560025",
                    "https://images.unsplash.com/photo-1633681926022-84c23e8cb2d6?w=800&q=80",
                    0.018, -0.012
            ),
            new DemoShopTemplate(
                    "Serenity Spa & Salon", "Relaxing spa treatments paired with expert salon services.",
                    "23 Indiranagar 100ft Road", "Bengaluru", "Karnataka", "560038",
                    "https://images.unsplash.com/photo-1580618672591-eb180b1a973f?w=800&q=80",
                    -0.020, -0.006
            ),
            new DemoShopTemplate(
                    "Style Studio Unisex Salon", "Trendy salon for haircuts, coloring and styling.",
                    "56 Koramangala 5th Block", "Bengaluru", "Karnataka", "560095",
                    "https://images.unsplash.com/photo-1595476108010-b4d1f102b1b1?w=800&q=80",
                    0.006, 0.020
            ),
    };

    @Transactional
    public List<DemoShopDto> seedDemoShops(double latitude, double longitude) {
        Users owner = findOrCreateDemoOwner();

        clearExistingDemoData(owner);

        List<DemoShopDto> created = new ArrayList<>();

        for (int i = 0; i < TEMPLATES.length; i++) {
            DemoShopTemplate template = TEMPLATES[i];

            BigDecimal shopLat = BigDecimal.valueOf(latitude + template.latOffset);
            BigDecimal shopLon = BigDecimal.valueOf(longitude + template.lonOffset);

            SalonShop shop = SalonShop.builder()
                    .owner(owner)
                    .shopName(template.name)
                    .shopDescription(template.description)
                    .phone("9800000" + (100 + i))
                    .isActive(true)
                    .coverImage(template.coverImage)
                    .address1(template.address1)
                    .city(template.city)
                    .state(template.state)
                    .postalCode(template.postalCode)
                    .country("India")
                    .latitude(shopLat)
                    .longitude(shopLon)
                    .build();

            SalonShop savedShop = salonShopRepository.save(shop);

            List<DemoShopServiceDto> serviceDtos = new ArrayList<>();
            serviceDtos.add(createDemoService(savedShop, "Haircut & Styling",
                    "Professional haircut with wash and blow-dry styling.",
                    30, new BigDecimal("299.00"), true, i, 0));
            serviceDtos.add(createDemoService(savedShop, "Hair Spa & Treatment",
                    "Nourishing hair spa treatment for healthy, shiny hair.",
                    45, new BigDecimal("799.00"), false, i, 1));

            created.add(DemoShopDto.builder()
                    .shopId(savedShop.getShopId())
                    .shopName(savedShop.getShopName())
                    .shopDescription(savedShop.getShopDescription())
                    .phone(savedShop.getPhone())
                    .coverImage(savedShop.getCoverImage())
                    .address1(savedShop.getAddress1())
                    .city(savedShop.getCity())
                    .state(savedShop.getState())
                    .latitude(savedShop.getLatitude().doubleValue())
                    .longitude(savedShop.getLongitude().doubleValue())
                    .distanceKm(haversineKm(latitude, longitude,
                            savedShop.getLatitude().doubleValue(), savedShop.getLongitude().doubleValue()))
                    .services(serviceDtos)
                    .build());
        }

        return created;
    }

    @Transactional(readOnly = true)
    public List<DemoShopDto> getDemoShops(double latitude, double longitude) {
        Users owner = authRepository.findByEmail(DEMO_OWNER_EMAIL);
        if (owner == null) {
            return List.of();
        }

        List<SalonShop> shops = salonShopRepository.findByOwner_UserId(owner.getUserId());

        List<DemoShopDto> result = new ArrayList<>();
        for (SalonShop shop : shops) {
            List<ShopService> services = shopServiceRepository.findByShop_ShopId(shop.getShopId());
            List<DemoShopServiceDto> serviceDtos = services.stream()
                    .map(this::toServiceDto)
                    .toList();

            result.add(DemoShopDto.builder()
                    .shopId(shop.getShopId())
                    .shopName(shop.getShopName())
                    .shopDescription(shop.getShopDescription())
                    .phone(shop.getPhone())
                    .coverImage(shop.getCoverImage())
                    .address1(shop.getAddress1())
                    .city(shop.getCity())
                    .state(shop.getState())
                    .latitude(shop.getLatitude().doubleValue())
                    .longitude(shop.getLongitude().doubleValue())
                    .distanceKm(haversineKm(latitude, longitude,
                            shop.getLatitude().doubleValue(), shop.getLongitude().doubleValue()))
                    .services(serviceDtos)
                    .build());
        }

        result.sort((a, b) -> Double.compare(a.getDistanceKm(), b.getDistanceKm()));
        return result;
    }

    private DemoShopServiceDto createDemoService(SalonShop shop, String name, String description,
                                                  int durationMinutes, BigDecimal price,
                                                  boolean isSmartQueueEnabled, int shopIndex, int serviceIndex) {
        ShopService service = ShopService.builder()
                .shop(shop)
                .name(name)
                .description(description)
                .durationMinutes(durationMinutes)
                .price(price)
                .isActive(true)
                .isSmartQueueEnabled(isSmartQueueEnabled)
                .build();

        ShopService savedService = shopServiceRepository.save(service);

        String imageUrl = SERVICE_IMAGE_POOL[(shopIndex * 2 + serviceIndex) % SERVICE_IMAGE_POOL.length];
        ShopServiceImage image = ShopServiceImage.builder()
                .service(savedService)
                .imageUrl(imageUrl)
                .sequence(0)
                .isDeleted(false)
                .build();
        shopServiceImageRepository.save(image);

        return toServiceDto(savedService);
    }

    private DemoShopServiceDto toServiceDto(ShopService service) {
        List<String> images = service.getImages().stream()
                .filter(img -> !img.isDeleted())
                .map(ShopServiceImage::getImageUrl)
                .toList();

        return DemoShopServiceDto.builder()
                .serviceId(service.getServiceId())
                .name(service.getName())
                .description(service.getDescription())
                .durationMinutes(service.getDurationMinutes())
                .price(service.getPrice())
                .isSmartQueueEnabled(service.getIsSmartQueueEnabled())
                .images(images)
                .build();
    }

    private Users findOrCreateDemoOwner() {
        Users existing = authRepository.findByEmail(DEMO_OWNER_EMAIL);
        if (existing != null) {
            return existing;
        }

        Users owner = Users.builder()
                .name("Demo Salon Owner")
                .email(DEMO_OWNER_EMAIL)
                .password(passwordEncoder.encode("DemoOwner@123"))
                .build();

        return authRepository.save(owner);
    }

    private void clearExistingDemoData(Users owner) {
        List<SalonShop> existingShops = salonShopRepository.findByOwner_UserId(owner.getUserId());
        for (SalonShop shop : existingShops) {
            List<ShopService> services = shopServiceRepository.findByShop_ShopId(shop.getShopId());
            for (ShopService service : services) {
                shopServiceImageRepository.deleteAll(service.getImages());
            }
            shopServiceRepository.deleteAll(services);
            salonShopRepository.delete(shop);
        }
    }

    private double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        double earthRadiusKm = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Math.round(earthRadiusKm * c * 100.0) / 100.0;
    }

    private static class DemoShopTemplate {
        final String name;
        final String description;
        final String address1;
        final String city;
        final String state;
        final String postalCode;
        final String coverImage;
        final double latOffset;
        final double lonOffset;

        DemoShopTemplate(String name, String description, String address1, String city, String state,
                          String postalCode, String coverImage, double latOffset, double lonOffset) {
            this.name = name;
            this.description = description;
            this.address1 = address1;
            this.city = city;
            this.state = state;
            this.postalCode = postalCode;
            this.coverImage = coverImage;
            this.latOffset = latOffset;
            this.lonOffset = lonOffset;
        }
    }
}
