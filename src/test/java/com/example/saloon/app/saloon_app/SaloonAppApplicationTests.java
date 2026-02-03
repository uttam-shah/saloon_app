package com.example.saloon.app.saloon_app;

import com.example.saloon.app.saloon_app.entity.Appointment;
import com.example.saloon.app.saloon_app.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@SpringBootTest
class SaloonAppApplicationTests {

    private final AppointmentRepository appointmentRepository;

    @Test
    void testFindAppointmentsByShopAndDateRange() {

        LocalDate date = LocalDate.of(2026, 2, 4);

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end   = date.plusDays(1).atStartOfDay();

        List<Appointment> appointments =
                appointmentRepository.findAppointmentsByShopAndDateRange(
                        "03d7cf5c-8582-476d-ae29-722333e9c149",
                        start,
                        end
                );

        appointments.forEach(a ->
                System.out.println(a.getAppointmentTime())
        );
    }
}
