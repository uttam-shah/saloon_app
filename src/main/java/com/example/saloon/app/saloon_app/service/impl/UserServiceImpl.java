package com.example.saloon.app.saloon_app.service.impl;

import com.example.saloon.app.saloon_app.dto.AddUserRequestDto;
import com.example.saloon.app.saloon_app.dto.UserDto;
import com.example.saloon.app.saloon_app.entity.Users;
import com.example.saloon.app.saloon_app.repository.UserRepository;
import com.example.saloon.app.saloon_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<UserDto> getAllUsers() {
        List<Users> users = userRepository.findAll();
        List<UserDto> userDto = users
                .stream()
                .map(user -> new UserDto(user.getUserId(), user.getName(), user.getEmail(), user.getPhone()))
                .toList();
        return userDto;
    }

    @Override
    public UserDto getUserById(Long id) {
       Users user =  userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("user not found with Id: "+id));
       UserDto userDto = modelMapper.map(user, UserDto.class);
       return  userDto;
    }

    @Override
    public UserDto createNewStudent(AddUserRequestDto addUserRequestDto) {
        Users newUser = modelMapper.map(addUserRequestDto, Users.class);
        Users user = userRepository.save(newUser);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.existsById(id);
        userRepository.deleteById(id);
    }

    @Override
    public UserDto updateUser(Long id, AddUserRequestDto addUserRequestDto) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with Id: "+ id));

        modelMapper.map(addUserRequestDto, user);

        user = userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updatePartialUser(Long id, Map<String, Objects> updates) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with Id: "+ id));

        return null;
    }
}
