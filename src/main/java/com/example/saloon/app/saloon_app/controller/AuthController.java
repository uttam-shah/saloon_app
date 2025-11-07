package com.example.saloon.app.saloon_app.controller;

import com.example.saloon.app.saloon_app.dto.RegisterUserDto;
import com.example.saloon.app.saloon_app.dto.UserDto;
import com.example.saloon.app.saloon_app.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;

   @PostMapping("/register_user")
    private ResponseEntity<UserDto> registerUser(@RequestBody @Valid RegisterUserDto registerUserDto){
       return  ResponseEntity.ok(authService.registerUser(registerUserDto));
   }
}
