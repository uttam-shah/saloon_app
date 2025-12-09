package com.example.saloon.app.saloon_app.dto.saloonShop.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class OwnerDto {
    private String userId;
    private String name;
    private String email;
    private String phone;
}
