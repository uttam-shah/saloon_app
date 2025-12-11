package com.example.saloon.app.saloon_app.dto.saloonShop;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RegisterShopDto {

    @NotBlank(message = "ownerId is Required")
    private String ownerId;

    @NotBlank(message = "Shop Name is Required")
    private String shopName;

    @NotBlank(message = "Shop Description is required")
    private String shopDescription;

    private String phone;

    private boolean isActive = false;

    private int serviceCount = 0;

    private  int appointmentsToday = 0;

    private int todayEarnings = 0;

    private String coverImage;

    private List<String> photos;

    private String address1;

    private String address2;

    @NotBlank(message = "city is Required")
    private String city;

    @NotBlank(message = "State is Required")
    private String state;

    @NotBlank(message = "Pin code is Required")
    private String postalCode;

    @NotBlank(message = "Country is Required")
    private String country;

    private BigDecimal latitude;

    private BigDecimal longitude;

}
