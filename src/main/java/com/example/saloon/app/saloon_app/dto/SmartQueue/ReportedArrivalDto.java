package com.example.saloon.app.saloon_app.dto.SmartQueue;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReportedArrivalDto {
    private BigDecimal lat;
    private BigDecimal lng;
    private Integer travelTimeRemainingMinutes;
}
