package com.example.saloon.app.saloon_app.dto.SmartQueue;

import lombok.Data;

import java.util.List;

@Data
public class OwnerQueueViewDto {
    private String shopId;
    private int totalWaiting;
    private List<SmartQueueEntryResponseDto> entries;
}
