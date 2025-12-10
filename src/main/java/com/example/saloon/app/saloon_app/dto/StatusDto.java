package com.example.saloon.app.saloon_app.dto;

import com.example.saloon.app.saloon_app.entity.Users;
import lombok.Data;

@Data
public class StatusDto {
    private boolean success;
    private String message;
    private UserDto data;
}
