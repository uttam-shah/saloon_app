package com.example.saloon.app.saloon_app.service.impl;

import com.example.saloon.app.saloon_app.dto.NotificationLogResponseDto;
import com.example.saloon.app.saloon_app.entity.NotificationLog;
import com.example.saloon.app.saloon_app.entity.NotificationType;
import com.example.saloon.app.saloon_app.entity.Users;
import com.example.saloon.app.saloon_app.exception.ForbiddenException;
import com.example.saloon.app.saloon_app.exception.ResourceNotFoundException;
import com.example.saloon.app.saloon_app.repository.NotificationLogRepository;
import com.example.saloon.app.saloon_app.service.NotificationLogService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationLogServiceImpl implements NotificationLogService {

    private final NotificationLogRepository notificationLogRepository;
    private final ModelMapper modelMapper;

    @Override
    public NotificationLog record(Users user, NotificationType type, String title, String body, String relatedEntityId) {
        NotificationLog log = NotificationLog.builder()
                .user(user)
                .type(type)
                .title(title)
                .body(body)
                .relatedEntityId(relatedEntityId)
                .isRead(false)
                .pushDelivered(false)
                .build();

        return notificationLogRepository.save(log);
    }

    @Override
    public void markPushDelivered(String notificationId, boolean delivered) {
        notificationLogRepository.findById(notificationId).ifPresent(log -> {
            log.setPushDelivered(delivered);
            notificationLogRepository.save(log);
        });
    }

    @Override
    public List<NotificationLogResponseDto> getForUser(String userId) {
        return notificationLogRepository.findByUser_UserIdOrderByCreatedAtDesc(userId).stream()
                .map(log -> modelMapper.map(log, NotificationLogResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void markRead(String notificationId, String userId) {
        NotificationLog log = notificationLogRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        if (!log.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("You do not have access to this notification");
        }

        log.setIsRead(true);
        notificationLogRepository.save(log);
    }
}
