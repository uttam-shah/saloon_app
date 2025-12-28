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
    private String imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private SalonShop shop;

    // only 10 photos are allowed
    @Column(nullable = false)
    private int sequence;

    @Column(nullable = false, updatable = false)
    private String imageUrl;

    @Column(nullable = false)
    private boolean isDeleted = false;
}
