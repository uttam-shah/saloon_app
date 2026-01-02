package com.example.saloon.app.saloon_app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "salon_shop")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalonShop {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "shop_id", length = 36, nullable = false, updatable = false)
    private String shopId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Users owner;

    @Column(name = "shop_name", length = 150, nullable = false)
    private String shopName;

    @Column(name = "shop_description", length = 350, nullable = false)
    private String shopDescription;

    @Column(name = "phone", length = 50, nullable = true)
    private String phone;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "service_count")
    private int serviceCount = 0;

    @Column(name = "appointments_today")
    private  int appointmentsToday = 0;

    @Column(name = "today_earnings")
    private int todayEarnings = 0;

    @Column(name = "cover_image")
    private String coverImage;

    @OneToMany(
            mappedBy = "shop",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("sequence ASC")
    private List<ShopImages> photos = new LinkedList<>();


    @Column(name = "address1", length = 255, nullable = false)
    private String address1;

    @Column(name = "address2", length = 255)
    private String address2;

    @Column(name = "city", length = 100, nullable = false)
    private String city;

    @Column(name = "state", length = 100, nullable = false)
    private String state;

    @Column(name = "postal_code", length = 20, nullable = false)
    private String postalCode;

    @Column(name = "country", length = 100, nullable = false)
    private String country;

    @Column(name = "latitude", precision = 9, scale = 6, nullable = false)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 9, scale = 6, nullable = false)
    private BigDecimal longitude;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(
            mappedBy = "shop",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("dayOfWeek ASC")
    private List<ShopOpeningHours> openingHours = new ArrayList<>();

    public void addPhoto(ShopImages photo) {
        photos.add(photo);
        photo.setShop(this);
    }

    // Helper method to remove photo
    public void removePhoto(ShopImages photo) {
        photos.remove(photo);
        photo.setShop(null);
    }


}

