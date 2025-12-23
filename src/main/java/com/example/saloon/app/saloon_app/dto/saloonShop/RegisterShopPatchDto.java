package com.example.saloon.app.saloon_app.dto.saloonShop;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

@Data
public class RegisterShopPatchDto {
    private String ownerId;
    private String shopName;
    private String shopDescription;
    private String phone;

    private Boolean isActive;              // BOOLEAN → must be nullable for PATCH
    private Integer serviceCount;          // INTEGER → must be nullable
    private Integer appointmentsToday;     // INTEGER → must be nullable
    private Integer todayEarnings;         // INTEGER → must be nullable

    private String address1;
    private String address2;

    private String city;
    private String state;
    private String postalCode;
    private String country;

    private BigDecimal latitude;
    private BigDecimal longitude;
}
