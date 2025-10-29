package com.example.saloon.app.saloon_app.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "shop_rating")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopRating {

    @Id
    @Column(name = "rating_id", length = 36, nullable = false, updatable = false)
    private String ratingId = UUID.randomUUID().toString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private SalonShop shop;

    @Column(name = "score", nullable = false)
    private Byte score; // 1–5 range enforced in application logic or DB

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
