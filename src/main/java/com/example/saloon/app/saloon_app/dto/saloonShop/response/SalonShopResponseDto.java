package com.example.saloon.app.saloon_app.dto.saloonShop.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalonShopResponseDto {
    private String shopId;
    private String shopName;
    private String shopDescription;
    private String phone;
    private boolean isActive = false;
    private int serviceCount = 0;
    private  int appointmentsToday = 0;
    private int todayEarnings = 0;
    private String coverImage;
    private List<ShopImageDto> photos;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private OwnerDto owner;
}


