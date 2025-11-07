package com.example.saloon.app.saloon_app.service;

import com.example.saloon.app.saloon_app.dto.RegisterUserDto;
import com.example.saloon.app.saloon_app.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

public interface AuthService {
    UserDto registerUser(RegisterUserDto registerUserDto);
}
