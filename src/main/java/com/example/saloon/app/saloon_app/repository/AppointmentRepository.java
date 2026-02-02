package com.example.saloon.app.saloon_app.repository;

import com.example.saloon.app.saloon_app.entity.Appointment;
import com.example.saloon.app.saloon_app.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {

    List<Appointment> findByUser_UserId(String userId);


    @Query("""
    SELECT a FROM Appointment a
    WHERE a.shop.shopId = :shopId
    AND DATE(a.AppointmentTime) = :date
    AND a.status = :status
""")
    List<Appointment> findAppointmentsByShopAndDate(
            @Param("shopId") String shopId,
            @Param("date") LocalDate date,
            @Param("status") AppointmentStatus status
    );

//
//    @Query("""
//    SELECT a FROM Appointment a
//    WHERE a.shop.shopId = :shopId
//    AND DATE(a.scheduledDateTime) = :date
//    AND a.status = :status
//""")
//    List<Appointment> findAppointmentsByShopAndDate(
//            String shopId,
//            LocalDate date,
//            AppointmentStatus status
//    );




}
