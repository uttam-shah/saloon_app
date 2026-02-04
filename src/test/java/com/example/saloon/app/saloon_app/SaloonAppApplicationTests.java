package com.example.saloon.app.saloon_app;

import com.example.saloon.app.saloon_app.entity.Appointment;
import com.example.saloon.app.saloon_app.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@SpringBootTest
class SaloonAppApplicationTests {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Test
    void testFindAppointmentsByShopAndDateRange() {

        System.out.println("Test method");

        LocalDate date = LocalDate.of(2026, 2, 4);

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end   = date.plusDays(1).atStartOfDay();

        List<Appointment> appointments =
                appointmentRepository.findAppointmentsByShopAndDateRange(
                        "03d7cf5c-8582-476d-ae29-722333e9c149",
                        start,
                        end
                );

        System.out.println(appointments.size()+ " appointments found");

        appointments.forEach(a ->
                System.out.println(a.getAppointmentTime())
        );
    }
}
