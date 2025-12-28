package com.example.saloon.app.saloon_app.repository;

import com.example.saloon.app.saloon_app.entity.SalonShop;
import com.example.saloon.app.saloon_app.entity.ShopImages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopImagesRepository extends JpaRepository<ShopImages, String> {
}
