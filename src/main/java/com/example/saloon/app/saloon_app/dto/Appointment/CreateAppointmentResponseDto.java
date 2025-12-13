package com.example.saloon.app.saloon_app.dto.Appointment;

import com.example.saloon.app.saloon_app.dto.ShopService.ShopServiceDto;
import com.example.saloon.app.saloon_app.dto.ShopService.ShopServiceResponseDto;
import com.example.saloon.app.saloon_app.dto.UserDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.response.SalonShopResponseDto;
import com.example.saloon.app.saloon_app.entity.AppointmentStatus;
import com.example.saloon.app.saloon_app.entity.Users;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateAppointmentResponseDto {

    private String AppointmentId;
    private UserDto user;
    private SalonShopResponseDto shop;
    private ShopServiceResponseDto service;
    private LocalDateTime AppointmentTime;

    private AppointmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
