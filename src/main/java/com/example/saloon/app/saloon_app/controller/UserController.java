package com.example.saloon.app.saloon_app.controller;

import com.example.saloon.app.saloon_app.dto.UserDto;
import com.example.saloon.app.saloon_app.entity.Users;
import com.example.saloon.app.saloon_app.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/user")
    public List<Users> getUser(){
        return userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    public UserDto getUserById(){
        return new UserDto(
                4l,
                "uttam",
                "shah",
                "uttam@gmail.com",
                "1234567890"
                );
    }
}
