package com.example.saloon.app.saloon_app.controller;


import com.example.saloon.app.saloon_app.dto.Appointment.CreateAppointmentDto;
import com.example.saloon.app.saloon_app.dto.Appointment.CreateAppointmentResponseDto;
import com.example.saloon.app.saloon_app.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    // Post method to create an appointment
    @PostMapping
    public ResponseEntity<CreateAppointmentResponseDto> createAppointment(
            @RequestBody @Valid CreateAppointmentDto dto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(appointmentService.createAppointment(dto));
    }

}
