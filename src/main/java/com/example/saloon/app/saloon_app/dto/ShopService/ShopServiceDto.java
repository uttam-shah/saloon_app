package com.example.saloon.app.saloon_app.dto.ShopService;

import com.example.saloon.app.saloon_app.entity.SalonShop;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShopServiceDto {
    @NotBlank(message = "ShopId is Required")
    private String shopId;

    @NotBlank(message = "Name is Required")
    private String name;

    @NotBlank(message = "Description is Required")
    private String description;

    @NotNull(message = "DurationMinutes is Required")
    private Integer durationMinutes;

    @NotNull(message = "Price is Required")
    private BigDecimal price;

    @NotNull(message = "Active Status is Required")
    private Boolean isActive;

    private Boolean isSmartQueueEnabled = false;
}
