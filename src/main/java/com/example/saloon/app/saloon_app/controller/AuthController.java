package com.example.saloon.app.saloon_app.controller;

import com.example.saloon.app.saloon_app.dto.*;
import com.example.saloon.app.saloon_app.repository.SalonShopRepository;
import com.example.saloon.app.saloon_app.service.AuthService;
import jakarta.validation.Valid;
import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;

   @PostMapping("/register_user")
    private ResponseEntity<UserDto> registerUser(@RequestBody @Valid RegisterUserDto registerUserDto){
//       return  ResponseEntity.ok(authService.registerUser(registerUserDto));
       return ResponseEntity
               .status(HttpStatus.CREATED)
               .body(authService.registerUser(registerUserDto));
   }

   @PostMapping("/login_user")
   private ResponseEntity<StatusDto> loginUser(@RequestBody @Valid LoginUserDto loginUserDto){
       System.out.println("/login_user");
       return ResponseEntity
               .status(HttpStatus.OK)
               .body(authService.loginUser(loginUserDto));
   }

//   @PostMapping("/register_shop")
//    private ResponseEntity<RegisterShopDto> registerShop(@RequestBody @Valid RegisterShopDto registerShopDto){
//       return ResponseEntity
//               .status(HttpStatus.OK)
//               .body(authService.registerShop(registerShopDto));
//   }
}
