package com.example.saloon.app.saloon_app.dto.SmartQueue;

import com.example.saloon.app.saloon_app.entity.TravelMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JoinQueueDto {

    @NotBlank(message = "shopId is Required")
    private String shopId;

    @NotBlank(message = "serviceId is Required")
    private String serviceId;

    @NotNull(message = "travelMode is Required")
    private TravelMode travelMode;

    private Integer initialTravelTimeMinutes;
}
