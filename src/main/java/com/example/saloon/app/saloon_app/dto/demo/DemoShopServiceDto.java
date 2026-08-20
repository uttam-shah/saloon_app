package com.example.saloon.app.saloon_app.dto.demo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemoShopServiceDto {
    private String serviceId;
    private String name;
    private String description;
    private Integer durationMinutes;
    private BigDecimal price;
    private Boolean isSmartQueueEnabled;
    private List<String> images;
}
