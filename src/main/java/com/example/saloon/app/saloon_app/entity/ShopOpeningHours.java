package com.example.saloon.app.saloon_app.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "shop_opening_hours")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopOpeningHours {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private SalonShop shop;

    @Column()
    private DayOfWeek dayOfWeek;

    @Column()
    private boolean open;

    private LocalTime openTime;
    private LocalTime closeTime;
}
