package com.example.saloon.app.saloon_app.dto.Appointment;

import com.example.saloon.app.saloon_app.entity.AppointmentStatus;
import com.example.saloon.app.saloon_app.entity.SalonShop;
import com.example.saloon.app.saloon_app.entity.ShopService;
import com.example.saloon.app.saloon_app.entity.Users;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
public class CreateAppointmentDto {

    @NotBlank(message = "userId is Required")
    private String userId;

    @NotBlank(message = "shopId is Required")
    private String shopId;

    @NotBlank(message = "serviceId is Requierd")
    private String serviceId;


    @NotNull(message = "scheduledDatetime is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime AppointmentTime;  // or rename to scheduledDatetime

//    private AppointmentStatus status;
}
