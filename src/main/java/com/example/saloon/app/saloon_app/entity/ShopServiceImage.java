package com.example.saloon.app.saloon_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shop_service_image")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopServiceImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "image_id", nullable = false, updatable = false)
    private String imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private ShopService service;

    @Column(name = "sequence")
    private int sequence;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
}
