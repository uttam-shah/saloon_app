package com.example.saloon.app.saloon_app.service.impl;

import com.example.saloon.app.saloon_app.dto.Appointment.CreateAppointmentDto;
import com.example.saloon.app.saloon_app.dto.Appointment.PatchAppointmentDto;
import com.example.saloon.app.saloon_app.dto.Appointment.TimeSlotDto;
import com.example.saloon.app.saloon_app.dto.Appointment.appointmentResponseDto;
import com.example.saloon.app.saloon_app.dto.ShopService.ShopServiceResponseDto;
import com.example.saloon.app.saloon_app.dto.UserDto;
import com.example.saloon.app.saloon_app.config.security.CurrentUser;
import com.example.saloon.app.saloon_app.dto.saloonShop.response.SalonShopResponseDto;
import com.example.saloon.app.saloon_app.entity.*;
import com.example.saloon.app.saloon_app.exception.ForbiddenException;
import com.example.saloon.app.saloon_app.repository.*;
import com.example.saloon.app.saloon_app.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private  final AppointmentRepository appointmentRepository;
    private final AuthRepository authRepository;
    private final SalonShopRepository salonShopRepository;
    private final ShopServiceRepository shopServiceRepository;
    private final ModelMapper modelMapper;

    private final ShopOpeningHoursRepository openingHourRepository;

//    @Override
//    public CreateAppointmentResponseDto createAppointment(CreateAppointmentDto dto) {
//        Appointment newAppointment = modelMapper.map(dto, Appointment.class);
//        newAppointment.setAppointmentId(null);
//
//        Users user = authRepository.findById(dto.getUserId())
//                .orElseThrow(() -> new RuntimeException("userId not found"));
//
//        SalonShop shop = salonShopRepository.findById(dto.getShopId())
//                .orElseThrow(() -> new RuntimeException("shopId not found"));
//
//        ShopService service = shopServiceRepository.findById(dto.getServiceId())
//                .orElseThrow(() -> new RuntimeException("serviceId not found"));
//
//        newAppointment.setUser(user);
//        newAppointment.setShop(shop);
//        newAppointment.setService(service);
//
//        Appointment saved = appointmentRepository.save(newAppointment);
//
//        CreateAppointmentResponseDto response =  modelMapper.map(saved, CreateAppointmentResponseDto.class);
//
//        response.setUser(modelMapper.map(user, UserDto.class));
//        response.setShop(modelMapper.map(shop, SalonShopResponseDto.class));
//        response.setService(modelMapper.map(service, ShopServiceResponseDto.class));
//
//        return response;
//
//    }

    public appointmentResponseDto createAppointment(CreateAppointmentDto dto) {

        Appointment appointment = new Appointment();

        // Booking is always made as the authenticated caller — never trust a client-supplied userId.
        Users user = authRepository.findById(CurrentUser.id())
                .orElseThrow(() -> new RuntimeException("userId not found"));

        SalonShop shop = salonShopRepository.findById(dto.getShopId())
                .orElseThrow(() -> new RuntimeException("shopId not found"));

        ShopService service = shopServiceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new RuntimeException("serviceId not found"));

        appointment.setUser(user);
        appointment.setShop(shop);
        appointment.setService(service);
        appointment.setAppointmentTime(dto.getAppointmentTime());

        Appointment saved = appointmentRepository.save(appointment);

        appointmentResponseDto response = new appointmentResponseDto();
        response.setAppointmentId(saved.getAppointmentId());
        response.setAppointmentTime(saved.getAppointmentTime());
        response.setStatus(saved.getStatus());
        response.setCreatedAt(saved.getCreatedAt());
        response.setUpdatedAt(saved.getUpdatedAt());
        response.setUser(modelMapper.map(user, UserDto.class));
        response.setShop(modelMapper.map(shop, SalonShopResponseDto.class));
        response.setService(modelMapper.map(service, ShopServiceResponseDto.class));

        return response;
    }

    @Override
    public List<appointmentResponseDto> getAppointments(String userId) {
        Users user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("userId not found"));

        List<Appointment> appointmentList = appointmentRepository.findByUser_UserId(userId);

//        return modelMapper.map(appointmentList, CreateAppointmentResponseDto.class);

        return appointmentList.stream()
                .map(appointment -> modelMapper.map(appointment, appointmentResponseDto.class))
                .collect(Collectors.toList());
    }


    @Override
    public appointmentResponseDto patchAppointment(PatchAppointmentDto dto, String appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("appointmentId not found"));

        String currentUserId = CurrentUser.id();
        boolean isBookingCustomer = appointment.getUser().getUserId().equals(currentUserId);
        boolean isShopOwner = appointment.getShop().getOwner().getUserId().equals(currentUserId);
        if (!isBookingCustomer && !isShopOwner) {
            throw new ForbiddenException("You do not have access to this appointment");
        }

        if(dto.getStatus() != null) appointment.setStatus(dto.getStatus());
        if(dto.getAppointmentTime() != null) appointment.setAppointmentTime(dto.getAppointmentTime());

        Appointment saved = appointmentRepository.save(appointment);

        return modelMapper.map(saved, appointmentResponseDto.class);


    }


}
