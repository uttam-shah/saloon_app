package com.example.saloon.app.saloon_app.service.impl;

import com.example.saloon.app.saloon_app.dto.Appointment.CreateAppointmentDto;
import com.example.saloon.app.saloon_app.dto.Appointment.CreateAppointmentResponseDto;
import com.example.saloon.app.saloon_app.dto.ShopService.ShopServiceResponseDto;
import com.example.saloon.app.saloon_app.dto.UserDto;
import com.example.saloon.app.saloon_app.dto.saloonShop.response.SalonShopResponseDto;
import com.example.saloon.app.saloon_app.entity.Appointment;
import com.example.saloon.app.saloon_app.entity.SalonShop;
import com.example.saloon.app.saloon_app.entity.ShopService;
import com.example.saloon.app.saloon_app.entity.Users;
import com.example.saloon.app.saloon_app.repository.AppointmentRepository;
import com.example.saloon.app.saloon_app.repository.AuthRepository;
import com.example.saloon.app.saloon_app.repository.SalonShopRepository;
import com.example.saloon.app.saloon_app.repository.ShopServiceRepository;
import com.example.saloon.app.saloon_app.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private  final AppointmentRepository appointmentRepository;
    private final AuthRepository authRepository;
    private final SalonShopRepository salonShopRepository;
    private final ShopServiceRepository shopServiceRepository;
    private final ModelMapper modelMapper;

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

    public CreateAppointmentResponseDto createAppointment(CreateAppointmentDto dto) {

        Appointment appointment = new Appointment();

        Users user = authRepository.findById(dto.getUserId())
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

        CreateAppointmentResponseDto response = new CreateAppointmentResponseDto();
        response.setAppointmentId(saved.getAppointmentId());
        response.setAppointmentTime(saved.getAppointmentTime());
        response.setUser(modelMapper.map(user, UserDto.class));
        response.setShop(modelMapper.map(shop, SalonShopResponseDto.class));
        response.setService(modelMapper.map(service, ShopServiceResponseDto.class));

        return response;
    }
}
