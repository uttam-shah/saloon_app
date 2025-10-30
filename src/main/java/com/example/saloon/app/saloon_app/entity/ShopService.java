//package com.example.saloon.app.saloon_app.entity;
//
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.ColumnDefault;
//
//import java.math.BigDecimal;
//import java.util.UUID;
//
//@Entity
//@Table(name = "shop_service")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class ShopService {
//
//    @Id
//    @Column(name = "service_id", length = 36, nullable = false, updatable = false)
//    private String serviceId = UUID.randomUUID().toString();
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "shop_id", nullable = false)
//    private SalonShop shop;
//
//    @Column(name = "name", length = 100, nullable = false)
//    private String name;
//
//    @Column(name = "description", columnDefinition = "TEXT")
//    private String description;
//
//    @Column(name = "duration_minutes", nullable = false)
//    private Integer durationMinutes;
//
//    @Column(name = "price", precision = 8, scale = 2, nullable = false)
//    private BigDecimal price;
//
//    @Column(name = "is_active", nullable = false)
//    @ColumnDefault("true")
//    private Boolean isActive = true;
//}
//
//
