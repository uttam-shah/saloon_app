package com.example.saloon.app.saloon_app.dto;

import com.example.saloon.app.saloon_app.entity.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationLogResponseDto {
    private String notificationId;
    private NotificationType type;
    private String title;
    private String body;
    private String relatedEntityId;
    private Boolean isRead;
    private Boolean pushDelivered;
    private LocalDateTime createdAt;
}
