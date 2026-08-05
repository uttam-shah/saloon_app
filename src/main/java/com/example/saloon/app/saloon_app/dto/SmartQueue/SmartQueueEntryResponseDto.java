package com.example.saloon.app.saloon_app.dto.SmartQueue;

import com.example.saloon.app.saloon_app.entity.SmartQueueStatus;
import com.example.saloon.app.saloon_app.entity.TravelMode;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SmartQueueEntryResponseDto {
    private String queueEntryId;
    private QueueShopLiteDto shop;
    private QueueServiceLiteDto service;
    private QueueUserLiteDto user;
    private LocalDateTime joinTime;
    private Integer queuePosition;
    private LocalDateTime estimatedStartTime;
    private LocalDateTime arrivalDeadline;
    private SmartQueueStatus status;
    private TravelMode travelMode;
    private LocalDateTime reachedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
