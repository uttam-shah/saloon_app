package com.example.saloon.app.saloon_app.dto.SmartQueue;

import com.example.saloon.app.saloon_app.entity.SmartQueueStatus;
import com.example.saloon.app.saloon_app.entity.TravelMode;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MyQueueStatusDto {
    private String queueEntryId;
    private QueueShopLiteDto shop;
    private QueueServiceLiteDto service;
    private LocalDateTime joinTime;
    private Integer queuePosition;
    private LocalDateTime estimatedStartTime;
    private LocalDateTime arrivalDeadline;
    private SmartQueueStatus status;
    private TravelMode travelMode;
    private LocalDateTime reachedAt;
    private int peopleAheadCount;
    private Long minutesUntilDeadline;
}
