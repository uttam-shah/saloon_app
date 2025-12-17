package com.example.saloon.app.saloon_app.dto.Appointment;

import com.example.saloon.app.saloon_app.entity.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PatchAppointmentDto {

    private String serviceId;    // optional

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime appointmentTime; // optional

    private AppointmentStatus status; // optional
}
