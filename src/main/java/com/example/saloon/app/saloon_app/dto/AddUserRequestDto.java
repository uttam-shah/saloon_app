package com.example.saloon.app.saloon_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AddUserRequestDto {
    private String name;
    private String email;
    private String phone;
    private String passwordHash;


}

