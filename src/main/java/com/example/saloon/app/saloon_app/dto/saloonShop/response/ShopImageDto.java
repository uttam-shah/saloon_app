package com.example.saloon.app.saloon_app.dto.saloonShop.response;

import com.example.saloon.app.saloon_app.entity.SalonShop;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ShopImageDto {
    private String imageId;
    private String shopId;
    private int sequence;
    private String imageUrl;
    private boolean isDeleted;
}
