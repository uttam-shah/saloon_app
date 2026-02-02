package com.example.saloon.app.saloon_app.dto.Appointment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlotDto {

    private LocalTime start;
    private LocalTime end;
    private boolean isAvailable;
}

