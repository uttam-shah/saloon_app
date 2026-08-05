package com.example.saloon.app.saloon_app.repository;

import com.example.saloon.app.saloon_app.entity.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, String> {
    List<NotificationLog> findByUser_UserIdOrderByCreatedAtDesc(String userId);
    List<NotificationLog> findByUser_UserIdAndIsReadFalseOrderByCreatedAtDesc(String userId);
}
