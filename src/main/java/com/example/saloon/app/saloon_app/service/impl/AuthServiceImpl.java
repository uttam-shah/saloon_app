package com.example.saloon.app.saloon_app.service.impl;

import com.example.saloon.app.saloon_app.config.security.JwtService;
import com.example.saloon.app.saloon_app.dto.*;
import com.example.saloon.app.saloon_app.entity.SalonShop;
import com.example.saloon.app.saloon_app.entity.Users;
import com.example.saloon.app.saloon_app.repository.AuthRepository;
import com.example.saloon.app.saloon_app.repository.SalonShopRepository;
import com.example.saloon.app.saloon_app.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final SalonShopRepository salonShopRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public UserDto registerUser(RegisterUserDto registerUserDto) {
        Users newUser = modelMapper.map(registerUserDto, Users.class);
        newUser.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        Users user = authRepository.save(newUser);

        return modelMapper.map(user, UserDto.class);
    }

//    @Override
//    public RegisterShopDto registerShop(RegisterShopDto registerShopDto) {
//        SalonShop newShop = modelMapper.map(registerShopDto, SalonShop.class);
//        Users user = authRepository.findById(registerShopDto.getOwnerId()).orElseThrow(() -> new RuntimeException("User not found"));;
//        newShop.setOwner(user);
//        SalonShop salonShop = salonShopRepository.save(newShop);
//
//        return  modelMapper.map(salonShop, RegisterShopDto.class);
//    }

    @Override
    public StatusDto loginUser(LoginUserDto loginUserDto) {
        StatusDto statusDto = new StatusDto();

        // --- 1. Validate phone or email ---
        boolean hasPhone = loginUserDto.getPhone() != null && !loginUserDto.getPhone().isBlank();
        boolean hasEmail = loginUserDto.getEmail() != null && !loginUserDto.getEmail().isBlank();

        if (!hasPhone && !hasEmail) {
            statusDto.setSuccess(false);
            statusDto.setMessage("Either Phone or Email is Required");
            return statusDto;
        }

        // --- 2. Fetch user ---
        Users newUser = hasPhone
                ? authRepository.findByPhone(loginUserDto.getPhone())
                : authRepository.findByEmail(loginUserDto.getEmail());

        if (newUser == null) {
            statusDto.setSuccess(false);
            statusDto.setMessage("User doesn't exist");
            return statusDto;
        }

        // --- 3. Validate password ---
        if (!passwordEncoder.matches(loginUserDto.getPassword(), newUser.getPassword())) {
            statusDto.setSuccess(false);
            statusDto.setMessage("Incorrect password");
            return statusDto;
        }

        // --- 4. Success ---
        statusDto.setSuccess(true);
        statusDto.setMessage("Login success");
        statusDto.setData(modelMapper.map(newUser, UserDto.class));
        statusDto.setToken(jwtService.generateToken(newUser));
        return statusDto;
    }

    @Override
    public void updateFcmToken(String userId, String fcmToken) {
        Users user = authRepository.findById(userId)
                .orElseThrow(() -> new com.example.saloon.app.saloon_app.exception.ResourceNotFoundException("User not found"));
        user.setFcmToken(fcmToken);
        authRepository.save(user);
    }


//    @Override
//    public StatusDto loginUser(LoginUserDto loginUserDto) {
//        Users newUser = null;
//        StatusDto statusDto = new StatusDto();
//        try {
//            if(!loginUserDto.getPhone().isEmpty()) {
//                newUser = authRepository.findByPhone(loginUserDto.getPhone());
//                if(newUser == null){
//                    statusDto.setSuccess(false);
//                    statusDto.setMessage("User Don't exist");
//                }
//            } else if (!loginUserDto.getEmail().isEmpty()) {
//                newUser = authRepository.findByEmail(loginUserDto.getEmail());
//                if(newUser == null){
//                    statusDto.setSuccess(false);
//                    statusDto.setMessage("User Don't exist");
//                }
//            }
//            else {
//                statusDto.setSuccess(false);
//                statusDto.setMessage("Either Phone or Email is Required");
//            }
//
//            if(newUser.getPassword().equals(loginUserDto.getPassword())) {
//                statusDto.setSuccess(true);
//                statusDto.setMessage("Login success");
//            }else {
//                statusDto.setSuccess(false);
//                statusDto.setMessage("Incorrect Password");
//            }
//
//        }catch (Exception e){
//            throw e;
//            statusDto.setSuccess(false);
//            statusDto.setMessage(e.getMessage());
//        }
//
//        return  statusDto;
//
//    }
}
