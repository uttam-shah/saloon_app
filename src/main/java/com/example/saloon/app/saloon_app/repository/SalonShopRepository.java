package com.example.saloon.app.saloon_app.repository;

import com.example.saloon.app.saloon_app.entity.SalonShop;
import com.example.saloon.app.saloon_app.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalonShopRepository extends JpaRepository<SalonShop, String> {

    List<SalonShop> getByOwner(Users owner);

    List<SalonShop> findByOwner_UserId(String userId);

}
