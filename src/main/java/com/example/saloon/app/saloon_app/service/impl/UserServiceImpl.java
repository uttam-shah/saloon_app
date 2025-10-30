package com.example.saloon.app.saloon_app.service.impl;

import com.example.saloon.app.saloon_app.dto.UserDto;
import com.example.saloon.app.saloon_app.entity.Users;
import com.example.saloon.app.saloon_app.repository.UserRepository;
import com.example.saloon.app.saloon_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        List<Users> users = userRepository.findAll();
        List<UserDto> userDto = users
                .stream()
                .map(user -> new UserDto(user.getUserId(), user.getName(), user.getEmail(), user.getPhone()))
                .toList();
        return List.of();
    }
}
