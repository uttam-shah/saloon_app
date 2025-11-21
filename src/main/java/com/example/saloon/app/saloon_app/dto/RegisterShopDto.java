package com.example.saloon.app.saloon_app.dto;

import com.example.saloon.app.saloon_app.entity.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RegisterShopDto {
    @NotNull
    private String ownerId;

    @NotNull
    private String shopName;

    @NotNull
    private String address1;

    private String address2;

    @NotNull
    private String city;

    @NotNull
    private String state;

    @NotNull
    private String postalCode;

    @NotNull
    private String country;

    @NotNull
    private BigDecimal latitude;

    @NotNull
    private BigDecimal longitude;
}
