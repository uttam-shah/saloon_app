package com.example.saloon.app.saloon_app.service;

import com.example.saloon.app.saloon_app.dto.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

public interface AuthService {
    StatusDto loginUser(@Valid LoginUserDto loginUserDto);

    UserDto registerUser(RegisterUserDto registerUserDto);

    void updateFcmToken(String userId, String fcmToken);

//    RegisterShopDto registerShop(@Valid RegisterShopDto registerShopDto);
}
