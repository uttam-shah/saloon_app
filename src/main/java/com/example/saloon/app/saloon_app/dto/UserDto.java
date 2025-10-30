package com.example.saloon.app.saloon_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private String userId;
    private String name;
    private String email;
    private String phone;


}
