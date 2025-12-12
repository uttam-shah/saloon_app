package com.example.saloon.app.saloon_app.dto.ShopService;
import com.example.saloon.app.saloon_app.entity.SalonShop;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ShopServiceResponseDto {
    private String serviceId;
    private SalonShopResponseDto shop;
    private String name;
    private String description;
    private Integer durationMinutes;
    private BigDecimal price;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

@Data
class SalonShopResponseDto {
    private String shopId;
    private String shopName;
    private String shopDescription;
    private String phone;
    private OwnerDto owner;
}

@Data
class OwnerDto {
    private String userId;
    private String name;
    private String email;
    private String phone;
}