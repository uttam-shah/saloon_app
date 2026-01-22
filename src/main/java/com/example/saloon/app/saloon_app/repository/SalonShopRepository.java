package com.example.saloon.app.saloon_app.repository;

import com.example.saloon.app.saloon_app.dto.saloonShop.ShopDistanceProjection;
import com.example.saloon.app.saloon_app.entity.SalonShop;
import com.example.saloon.app.saloon_app.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SalonShopRepository extends JpaRepository<SalonShop, String> {

    List<SalonShop> getByOwner(Users owner);

    List<SalonShop> findByOwner_UserId(String userId);

    /**
     * METHOD 1: Using Projection (RECOMMENDED)
     * Returns only the fields you need with distance
     * Cleaner and type-safe
     */
    @Query(value = """
        SELECT 
            s.shop_id as shopId,
            s.shop_name as shopName,
            s.shop_description as shopDescription,
            s.phone as phone,
            s.cover_image as coverImage,
            s.address1 as address1,
            s.address2 as address2,
            s.city as city,
            s.state as state,
            s.postal_code as postalCode,
            s.country as country,
            s.latitude as latitude,
            s.longitude as longitude,
            s.is_active as isActive,
            ST_Distance(
                s.location,
                ST_SetSRID(ST_MakePoint(:userLon, :userLat), 4326)::geography
            ) as distanceMeters
        FROM salon_shop s
        WHERE s.is_active = true
        AND ST_DWithin(
            s.location,
            ST_SetSRID(ST_MakePoint(:userLon, :userLat), 4326)::geography,
            :radiusMeters
        )
        ORDER BY distanceMeters
        LIMIT :limit
        """, nativeQuery = true)
    List<ShopDistanceProjection> findNearbyShops(
            @Param("userLat") double userLat,
            @Param("userLon") double userLon,
            @Param("radiusMeters") double radiusMeters,
            @Param("limit") int limit
    );

    /**
     * METHOD 2: Returns full entity with distance (if you need complete shop data)
     * Returns Object[] where:
     * Object[0] = SalonShop entity
     * Object[1] = Double distance
     */
    @Query(value = """
        SELECT s.*, 
               ST_Distance(
                   s.location,
                   ST_SetSRID(ST_MakePoint(:userLon, :userLat), 4326)::geography
               ) as distance
        FROM salon_shop s
        WHERE s.is_active = true
        AND ST_DWithin(
            s.location,
            ST_SetSRID(ST_MakePoint(:userLon, :userLat), 4326)::geography,
            :radiusMeters
        )
        ORDER BY distance
        LIMIT :limit
        """, nativeQuery = true)
    List<Object[]> findNearbyShopsWithEntity(
            @Param("userLat") double userLat,
            @Param("userLon") double userLon,
            @Param("radiusMeters") double radiusMeters,
            @Param("limit") int limit
    );

}
