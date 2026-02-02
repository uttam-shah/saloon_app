package com.example.saloon.app.saloon_app.service;

import com.example.saloon.app.saloon_app.dto.Appointment.TimeSlotDto;

import java.time.LocalDate;
import java.util.List;

public interface SlotService {

    List<TimeSlotDto> getAvailableSlots(
            String shopId,
            LocalDate date,
            int serviceDurationMinutes
    );
}
