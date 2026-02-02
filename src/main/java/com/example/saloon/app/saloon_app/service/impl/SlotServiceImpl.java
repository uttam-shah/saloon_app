package com.example.saloon.app.saloon_app.service.impl;

import com.example.saloon.app.saloon_app.dto.Appointment.TimeSlotDto;
import com.example.saloon.app.saloon_app.entity.Appointment;
import com.example.saloon.app.saloon_app.entity.AppointmentStatus;
import com.example.saloon.app.saloon_app.entity.ShopOpeningHours;
import com.example.saloon.app.saloon_app.repository.AppointmentRepository;
import com.example.saloon.app.saloon_app.repository.ShopOpeningHoursRepository;
import com.example.saloon.app.saloon_app.service.SlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SlotServiceImpl implements SlotService {

    private final ShopOpeningHoursRepository openingHourRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public List<TimeSlotDto> getAvailableSlots(
            String shopId,
            LocalDate date,
            int serviceDurationMinutes
    ) {

        DayOfWeek day = date.getDayOfWeek();

        ShopOpeningHours openingHour = openingHourRepository
                .findByShopIdAndDayOfWeek(shopId, day)
                .orElseThrow(() -> new RuntimeException("Shop closed"));

        if (!openingHour.isOpen()) {
            return List.of();
        }

        List<Appointment> appointments =
                appointmentRepository.findAppointmentsByShopAndDate(
                        shopId,
                        date,
                        AppointmentStatus.PENDING
                );

        List<TimeSlotDto> slots = generateSlots(
                openingHour.getOpenTime(),
                openingHour.getCloseTime(),
                serviceDurationMinutes
        );

        markUnavailableSlots(slots, appointments, date);

        return slots;
    }

    // ---------------------------
    // SLOT GENERATION
    // ---------------------------
    private List<TimeSlotDto> generateSlots(
            LocalTime openTime,
            LocalTime closeTime,
            int durationMinutes
    ) {

        List<TimeSlotDto> slots = new ArrayList<>();
        LocalTime cursor = openTime;

        while (!cursor.plusMinutes(durationMinutes).isAfter(closeTime)) {

            slots.add(new TimeSlotDto(
                    cursor,
                    cursor.plusMinutes(durationMinutes),
                    true
            ));

            cursor = cursor.plusMinutes(durationMinutes);
        }

        return slots;
    }

    // ---------------------------
    // AVAILABILITY CHECK
    // ---------------------------
    private void markUnavailableSlots(
            List<TimeSlotDto> slots,
            List<Appointment> appointments,
            LocalDate date
    ) {

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        for (TimeSlotDto slot : slots) {

            // ❌ Past time (today only)
            if (date.equals(today) && slot.getStart().isBefore(now)) {
                slot.setAvailable(false);
                continue;
            }

            for (Appointment appt : appointments) {

                boolean overlaps =
                        slot.getStart().isBefore(LocalTime.from(appt.getEndTime())) &&
                                slot.getEnd().isAfter(LocalTime.from(appt.getAppointmentTime()));

                if (overlaps) {
                    slot.setAvailable(false);
                    break; // 🚀 optimization
                }
            }
        }
    }
}
