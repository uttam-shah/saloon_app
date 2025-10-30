package com.example.saloon.app.saloon_app.repository;

import com.example.saloon.app.saloon_app.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
}
