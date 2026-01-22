package com.example.saloon.app.saloon_app.dto.saloonShop;


public interface ShopDistanceProjection {
    String getShopId();
    String getShopName();
    String getShopDescription();
    String getPhone();
    String getCoverImage();
    String getAddress1();
    String getAddress2();
    String getCity();
    String getState();
    String getPostalCode();
    String getCountry();
    Double getLatitude();
    Double getLongitude();
    Boolean getIsActive();
    Double getDistanceMeters(); // Distance in meters
}
