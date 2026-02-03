package com.example.saloon.app.saloon_app.repository;

import com.example.saloon.app.saloon_app.entity.Appointment;
import com.example.saloon.app.saloon_app.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {

    List<Appointment> findByUser_UserId(String userId);

    @Query("""
    SELECT a FROM Appointment a
    WHERE a.shop.shopId = :shopId
      AND a.AppointmentTime >= :start
      AND a.AppointmentTime < :end
""")
    List<Appointment> findAppointmentsByShopAndDateRange(
            @Param("shopId") String shopId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );


}
