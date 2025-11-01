package com.example.saloon.app.saloon_app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AddUserRequestDto {
    @NotBlank(message = "Name is Required")
    @Size(min = 3, max = 30, message = "Name should be of length of 3 to 30 character")
    private String name;

    @Email
    @NotBlank(message = "Email is Required")
    private String email;
    private String phone;
    private String passwordHash;


}

