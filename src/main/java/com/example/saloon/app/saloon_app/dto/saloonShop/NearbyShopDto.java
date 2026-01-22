package com.example.saloon.app.saloon_app.dto.saloonShop;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NearbyShopDto {
    private String shopId;
    private String shopName;
    private String shopDescription;
    private String phone;
    private String coverImage;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private Double latitude;
    private Double longitude;
    private Boolean isActive;
    private Double distanceKm;        // Distance in kilometers
    private Double distanceMeters;    // Distance in meters
}
