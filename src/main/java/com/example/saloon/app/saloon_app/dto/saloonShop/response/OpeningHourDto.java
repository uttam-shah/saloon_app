package com.example.saloon.app.saloon_app.dto.saloonShop.response;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpeningHourDto {
    private DayOfWeek dayOfWeek;  // "MONDAY", "TUESDAY", etc.
    private boolean open;
    private LocalTime openTime;   // "09:00"
    private LocalTime closeTime;  // "18:00"
}
