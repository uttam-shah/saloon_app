package com.example.saloon.app.saloon_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shop_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopImages {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "image_id", nullable = false, updatable = false)
    private String imageId;

    @Column(name = "shop_id")
    private String shopId;

    @Column(name = "image_url", updatable = false)
    private String imageUrl;

    @Column(name = "is_main", updatable = true)
    private boolean isMain;

    @Column(name = "is_deleted", nullable = true)
    private boolean isDeleted = false;
}
