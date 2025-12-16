package com.example.saloon.app.saloon_app.repository;

import com.example.saloon.app.saloon_app.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {

    List<Appointment> findByUser_UserId(String userId);

}
