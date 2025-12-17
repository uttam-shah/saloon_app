package com.example.saloon.app.saloon_app.service;

import com.example.saloon.app.saloon_app.dto.Appointment.CreateAppointmentDto;
import com.example.saloon.app.saloon_app.dto.Appointment.PatchAppointmentDto;
import com.example.saloon.app.saloon_app.dto.Appointment.appointmentResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface AppointmentService {
    appointmentResponseDto createAppointment(@Valid CreateAppointmentDto dto);

    List<appointmentResponseDto> getAppointments(String userId);

    appointmentResponseDto patchAppointment(PatchAppointmentDto dto, String appointmentId);
}
