package com.example.saloon.app.saloon_app.dto.ShopService;

import com.example.saloon.app.saloon_app.entity.SalonShop;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShopServiceResponseDto {
    private String serviceId;
    private String shopId;
    private String name;
    private String description;
    private Integer durationMinutes;
    private BigDecimal price;
    private Boolean isActive;
}
