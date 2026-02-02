package com.example.saloon.app.saloon_app.controller;

import com.example.saloon.app.saloon_app.dto.Appointment.TimeSlotDto;
import com.example.saloon.app.saloon_app.service.SlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/slot")
public class SlotController {

    private final SlotService slotService;

    @GetMapping("/available")
    public ResponseEntity<List<TimeSlotDto>> getSlots(
            @RequestParam String shopId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            @RequestParam int serviceDurationMinutes
    ) {

        return ResponseEntity.ok(
                slotService.getAvailableSlots(
                        shopId,
                        date,
                        serviceDurationMinutes
                )
        );
    }
}
