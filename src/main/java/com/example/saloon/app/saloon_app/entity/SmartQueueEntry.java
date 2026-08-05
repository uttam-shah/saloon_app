package com.example.saloon.app.saloon_app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "smart_queue_entry")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmartQueueEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "queue_entry_id", length = 36, nullable = false, updatable = false)
    private String queueEntryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private SalonShop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private ShopService service;

    @Column(name = "join_time", nullable = false)
    private LocalDateTime joinTime;

    @Column(name = "queue_position", nullable = false)
    private Integer queuePosition;

    @Column(name = "estimated_start_time")
    private LocalDateTime estimatedStartTime;

    @Column(name = "arrival_deadline")
    private LocalDateTime arrivalDeadline;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private SmartQueueStatus status = SmartQueueStatus.WAITING;

    @Enumerated(EnumType.STRING)
    @Column(name = "travel_mode", length = 20)
    private TravelMode travelMode;

    @Column(name = "reported_travel_time_minutes")
    private Integer reportedTravelTimeMinutes;

    @Column(name = "last_lat", precision = 9, scale = 6)
    private BigDecimal lastLat;

    @Column(name = "last_lng", precision = 9, scale = 6)
    private BigDecimal lastLng;

    @Column(name = "reached_at")
    private LocalDateTime reachedAt;

    @Column(name = "service_started_at")
    private LocalDateTime serviceStartedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
