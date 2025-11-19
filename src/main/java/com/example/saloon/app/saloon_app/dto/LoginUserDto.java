package com.example.saloon.app.saloon_app.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginUserDto {

    private String phone;
    @Email
    private String email;
    @NotBlank(message = "Password is Required")
    private String password;

}
