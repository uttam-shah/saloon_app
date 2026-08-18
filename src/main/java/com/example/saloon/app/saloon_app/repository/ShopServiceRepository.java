package com.example.saloon.app.saloon_app.repository;

import com.example.saloon.app.saloon_app.entity.SalonShop;
import com.example.saloon.app.saloon_app.entity.ShopService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopServiceRepository extends JpaRepository<ShopService, String> {
    List<ShopService> findByShop_Owner_UserId(String userId);
    List<ShopService> findByShop_ShopId(String shopId);
    List<ShopService> findByShop_ShopIdInAndIsActiveTrue(List<String> shopIds);
}
