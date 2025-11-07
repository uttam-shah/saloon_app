package com.example.saloon.app.saloon_app.service.impl;

import com.example.saloon.app.saloon_app.dto.RegisterUserDto;
import com.example.saloon.app.saloon_app.dto.UserDto;
import com.example.saloon.app.saloon_app.entity.Users;
import com.example.saloon.app.saloon_app.repository.AuthRepository;
import com.example.saloon.app.saloon_app.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDto registerUser(RegisterUserDto registerUserDto) {
        Users newUser = modelMapper.map(registerUserDto, Users.class);
        Users user = authRepository.save(newUser);
        return modelMapper.map(user, UserDto.class);
    }
}
