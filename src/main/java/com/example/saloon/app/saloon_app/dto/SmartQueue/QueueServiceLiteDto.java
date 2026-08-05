package com.example.saloon.app.saloon_app.dto.SmartQueue;

import lombok.Data;

@Data
public class QueueServiceLiteDto {
    private String serviceId;
    private String name;
    private Integer durationMinutes;
}
