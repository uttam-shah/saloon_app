package com.example.saloon.app.saloon_app.dto.registerShop;

import com.example.saloon.app.saloon_app.entity.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RegisterShopDto {

    @NotBlank(message = "ownerId is Required")
    private String ownerId;

    @NotBlank(message = "Shop Name is Required")
    private String shopName;

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
