package com.example.saloon.app.saloon_app.service;

import com.example.saloon.app.saloon_app.dto.Appointment.CreateAppointmentDto;
import com.example.saloon.app.saloon_app.dto.Appointment.CreateAppointmentResponseDto;
import jakarta.validation.Valid;

public interface AppointmentService {
    CreateAppointmentResponseDto createAppointment(@Valid CreateAppointmentDto dto);
}
