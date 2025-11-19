package com.example.saloon.app.saloon_app.repository;

import com.example.saloon.app.saloon_app.entity.Users;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<Users, String> {
    Users findByPhone(String phone);
    Users findByEmail(String email);
}
