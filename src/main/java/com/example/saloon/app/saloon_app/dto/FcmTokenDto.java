package com.example.saloon.app.saloon_app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FcmTokenDto {
    @NotBlank(message = "fcmToken is Required")
    private String fcmToken;
}
