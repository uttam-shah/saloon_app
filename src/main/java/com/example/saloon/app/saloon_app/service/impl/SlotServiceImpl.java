package com.example.saloon.app.saloon_app.service.impl;

import com.example.saloon.app.saloon_app.dto.Appointment.TimeSlotDto;
import com.example.saloon.app.saloon_app.entity.Appointment;
import com.example.saloon.app.saloon_app.entity.AppointmentStatus;
import com.example.saloon.app.saloon_app.entity.SalonShop;
import com.example.saloon.app.saloon_app.entity.ShopOpeningHours;
import com.example.saloon.app.saloon_app.repository.AppointmentRepository;
import com.example.saloon.app.saloon_app.repository.SalonShopRepository;
import com.example.saloon.app.saloon_app.repository.ShopOpeningHoursRepository;
import com.example.saloon.app.saloon_app.service.SlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SlotServiceImpl implements SlotService {

    private final ShopOpeningHoursRepository openingHourRepository;
    private final AppointmentRepository appointmentRepository;
    private final SalonShopRepository salonShopRepository;

    @Override
    public List<TimeSlotDto> getAvailableSlots(
            String shopId,
            LocalDate date,
            int serviceDurationMinutes
    ) {

        DayOfWeek day = date.getDayOfWeek();

        SalonShop shop = salonShopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("shopId not found"));

//        shop.getOpeningHours().get(0).getDayOfWeek() == day;

        ShopOpeningHours appointmentDay = new ShopOpeningHours();

        for (ShopOpeningHours shopOpeningHours : shop.getOpeningHours()){
            if(shopOpeningHours.getDayOfWeek() == day){
                appointmentDay = shopOpeningHours;
                break;
            }
        }

        if(!appointmentDay.isOpen()){
            System.out.println("Shop Closed");
        }


        LocalDateTime start = date.atStartOfDay();              // 2026-02-04 00:00
        LocalDateTime end   = date.plusDays(1).atStartOfDay();  // 2026-02-05 00:00
        List<Appointment> appointments =
                appointmentRepository.findAppointmentsByShopAndDateRange(
                        shopId,
                        start,
                        end
                );

        List<TimeSlotDto> slots = generateSlots(
                appointmentDay.getOpenTime(),
                appointmentDay.getCloseTime(),
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

        System.out.println("Appointments"+ appointments);

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
