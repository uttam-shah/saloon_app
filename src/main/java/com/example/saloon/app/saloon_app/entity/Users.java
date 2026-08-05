package com.example.saloon.app.saloon_app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    @Column(name = "user_id", length = 36, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    @Column(name = "name", length = 100, nullable = false)
    public String name;

    @Column(name = "email", length = 150, nullable = false, unique = true)
    public String email;

    @Column(name = "phone", length = 20, unique = true)
    private String phone;

    @Column(name = "password", length = 256, nullable = false)
    private String password;

    @Column(name = "fcm_token", length = 512)
    private String fcmToken;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}



