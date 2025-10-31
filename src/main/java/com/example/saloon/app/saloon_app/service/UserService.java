package com.example.saloon.app.saloon_app.service;

import com.example.saloon.app.saloon_app.dto.AddUserRequestDto;
import com.example.saloon.app.saloon_app.dto.UserDto;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    UserDto createNewStudent(AddUserRequestDto addUserRequestDto);

    void deleteUserById(Long id);

    UserDto updateUser(Long id, AddUserRequestDto addUserRequestDto);

    UserDto updatePartialUser(Long id, Map<String, Objects> updates);
}
