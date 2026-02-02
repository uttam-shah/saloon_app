package com.example.saloon.app.saloon_app.repository;

import com.example.saloon.app.saloon_app.entity.ShopOpeningHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.DayOfWeek;
import java.util.Optional;

public interface ShopOpeningHoursRepository extends JpaRepository<ShopOpeningHours, String> {

//    Optional<ShopOpeningHours> findByShopIdAndDayOfWeek(
//            String shopId,
//            DayOfWeek dayOfWeek
//    );
    Optional<ShopOpeningHours> findByShopIdAndDayOfWeek(
            String shopId,
            DayOfWeek dayOfWeek
    );

}
