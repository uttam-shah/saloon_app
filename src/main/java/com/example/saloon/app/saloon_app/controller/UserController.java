package com.example.saloon.app.saloon_app.controller;

import com.example.saloon.app.saloon_app.dto.AddUserRequestDto;
import com.example.saloon.app.saloon_app.dto.UserDto;
import com.example.saloon.app.saloon_app.entity.Users;
import com.example.saloon.app.saloon_app.repository.UserRepository;
import com.example.saloon.app.saloon_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {

    private final UserService userService;
//    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUser(){
//        return userService.getAllUsers();
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){
//        return userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> createNewUser(@RequestBody @Valid AddUserRequestDto addUserRequestDto){
        System.out.println("create User");
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createNewStudent(addUserRequestDto));

    }

//    @PostMapping
//    public UserDto createNewUser(@RequestBody AddUserRequestDto addUserRequestDto){
//        return userService.createNewStudent(addUserRequestDto);
//
//    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody AddUserRequestDto addUserRequestDto){
        return ResponseEntity.ok(userService.updateUser(id, addUserRequestDto ));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updatePartialUser(@PathVariable Long id, @RequestBody Map<String, Object> updates ){
        System.out.println("path mapping");
        return ResponseEntity.ok(userService.updatePartialUser(id, updates));
    }

//    @PatchMapping("/{id}")
//    public ResponseEntity<UserDto> updatePartialUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
//        System.out.println("path mapping");
//        return ResponseEntity.ok(userService.updatePartialUser(id, updates));
//    }
}
