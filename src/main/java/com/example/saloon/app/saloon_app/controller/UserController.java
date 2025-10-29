package com.example.saloon.app.saloon_app.controller;

import com.example.saloon.app.saloon_app.dto.UserDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {


    @GetMapping("/user")
    public UserDto getUser(){
        return new UserDto(
                4l,
                "uttam",
                "shah",
                "uttam@gmail.com",
                "1234567890"
                );
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
