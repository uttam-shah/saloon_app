package com.example.saloon.app.saloon_app.controller;

import com.example.saloon.app.saloon_app.config.security.CurrentUser;
import com.example.saloon.app.saloon_app.dto.NotificationLogResponseDto;
import com.example.saloon.app.saloon_app.service.NotificationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationLogService notificationLogService;

    @GetMapping("/me")
    public ResponseEntity<List<NotificationLogResponseDto>> getMyNotifications() {
        return ResponseEntity.ok(notificationLogService.getForUser(CurrentUser.id()));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> markRead(@PathVariable String notificationId) {
        notificationLogService.markRead(notificationId, CurrentUser.id());
        return ResponseEntity.noContent().build();
    }
}
