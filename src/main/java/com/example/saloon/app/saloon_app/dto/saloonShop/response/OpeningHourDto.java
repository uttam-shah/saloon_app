package com.example.saloon.app.saloon_app.dto.saloonShop.response;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpeningHourDto {
    private String dayOfWeek;  // "MONDAY", "TUESDAY", etc.
    private boolean isOpen;
    private String openTime;   // "09:00"
    private String closeTime;  // "18:00"
}
