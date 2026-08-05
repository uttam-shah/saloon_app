package com.example.saloon.app.saloon_app.service;

import com.example.saloon.app.saloon_app.dto.NotificationLogResponseDto;
import com.example.saloon.app.saloon_app.entity.NotificationLog;
import com.example.saloon.app.saloon_app.entity.NotificationType;
import com.example.saloon.app.saloon_app.entity.Users;

import java.util.List;

public interface NotificationLogService {
    NotificationLog record(Users user, NotificationType type, String title, String body, String relatedEntityId);

    void markPushDelivered(String notificationId, boolean delivered);

    List<NotificationLogResponseDto> getForUser(String userId);

    void markRead(String notificationId, String userId);
}
