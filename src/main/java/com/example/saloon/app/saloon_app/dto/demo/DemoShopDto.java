package com.example.saloon.app.saloon_app.dto.demo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemoShopDto {
    private String shopId;
    private String shopName;
    private String shopDescription;
    private String phone;
    private String coverImage;
    private String address1;
    private String city;
    private String state;
    private Double latitude;
    private Double longitude;
    private Double distanceKm;
    private List<DemoShopServiceDto> services;
}
