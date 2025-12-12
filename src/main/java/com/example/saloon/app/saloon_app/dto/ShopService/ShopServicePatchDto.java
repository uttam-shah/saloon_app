package com.example.saloon.app.saloon_app.dto.ShopService;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShopServicePatchDto {
    private String name;
    private String description;
    private Integer durationMinutes;
    private BigDecimal price;
    private Boolean isActive;
}

