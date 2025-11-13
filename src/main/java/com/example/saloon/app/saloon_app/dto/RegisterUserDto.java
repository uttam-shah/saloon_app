package com.example.saloon.app.saloon_app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterUserDto {

    @NotBlank(message = "Name is Required")
    private String name;

    @NotBlank(message = "Phone is Required")
    private String phone;

    private String Dob;

    private String email;

    @NotBlank(message = "Password is Required")
    private String password;
}
