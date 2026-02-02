package com.example.saloon.app.saloon_app.controller;


import com.example.saloon.app.saloon_app.dto.Appointment.CreateAppointmentDto;
import com.example.saloon.app.saloon_app.dto.Appointment.PatchAppointmentDto;
import com.example.saloon.app.saloon_app.dto.Appointment.TimeSlotDto;
import com.example.saloon.app.saloon_app.dto.Appointment.appointmentResponseDto;
import com.example.saloon.app.saloon_app.service.AppointmentService;
import com.example.saloon.app.saloon_app.service.SlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final SlotService slotService;

    // Post method to create an appointment
    @PostMapping
    public ResponseEntity<appointmentResponseDto> createAppointment(
            @RequestBody @Valid CreateAppointmentDto dto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(appointmentService.createAppointment(dto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<appointmentResponseDto>> getAppointments(
           @PathVariable String userId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(appointmentService.getAppointments(userId));
    }

    @PatchMapping("/{appointmentId}")
    public ResponseEntity<appointmentResponseDto> patchAppointment(
            @PathVariable String appointmentId,
            @RequestBody PatchAppointmentDto dto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(appointmentService.patchAppointment(dto, appointmentId));
    }


}
